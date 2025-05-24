package com.example.stress_detection.controller;

import com.example.stress_detection.entity.FileInfo;
import com.example.stress_detection.result.Result;
import com.example.stress_detection.service.FileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

@RestController
@RequestMapping("/detection")
public class StressDetectController {

    @Autowired
    private FileInfoService fileInfoService;

    @Value("${wesad.dataDir}")
    private String wesadDataDir;

    private static final Pattern FOLDER_PATTERN = Pattern.compile("^S([2-9]|1[0-7])$");

    @PostMapping("/analyze")
    public ResponseEntity<?> detectStress(@RequestParam("file_id") Long fileId) {
        try {
            // 1. 获取文件信息
            FileInfo fileInfo = fileInfoService.getById(fileId);
            if (fileInfo == null) {
                return ResponseEntity.badRequest().body(Result.fail("文件不存在"));
            }

            String fileName = fileInfo.getFileName();
            String filePath = fileInfo.getFilePath();

            // 2. 检查是否是符合格式的文件夹名
            if (!FOLDER_PATTERN.matcher(fileName).matches()) {
                return ResponseEntity.badRequest().body(Result.fail("文件夹格式不正确"));
            }

            // 3. 获取文件夹中的文件列表
            File folder = new File(filePath);
            if (!folder.isDirectory()) {
                return ResponseEntity.badRequest().body(Result.fail("不是文件夹"));
            }

            // 4. 检查必需文件
            String subjectNumber = fileName.substring(1); // 获取S后面的数字
            System.out.println("------------------------------------subjectNumber = " + subjectNumber);
            Set<String> requiredFiles = new HashSet<>(Arrays.asList(
                "S" + subjectNumber + "_E4_Data.zip",
                "S" + subjectNumber + "_quest.csv",
                "S" + subjectNumber + "_readme.txt",
                "S" + subjectNumber + "_respiban.txt",
                "S" + subjectNumber + ".pkl"
            ));

            File[] files = folder.listFiles();
            Set<String> existingFiles = new HashSet<>();
            if (files != null) {
                for (File file : files) {
                    existingFiles.add(file.getName());
                }
            }

            if (!existingFiles.containsAll(requiredFiles)) {
                requiredFiles.removeAll(existingFiles);
                return ResponseEntity.badRequest().body(
                    Result.fail("缺少必需文件: " + String.join(", ", requiredFiles)));
            }

            // 5. 读取特征文件
            String featsFile = wesadDataDir + "/S" + subjectNumber + "_feats_4.csv";
            File featsFileObj = new File(featsFile);
            if (!featsFileObj.exists()) {
                return ResponseEntity.badRequest().body(Result.fail("特征文件不存在"));
            }

            // 6. 读取CSV文件的最后三列的所有值
            try (CSVReader reader = new CSVReader(new FileReader(featsFile))) {
                List<String[]> allRows = reader.readAll();
                if (allRows.isEmpty()) {
                    return ResponseEntity.badRequest().body(Result.fail("特征文件为空"));
                }

                // 获取所有行的最后三列
                List<List<Double>> columnData = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    columnData.add(new ArrayList<>());
                }

                for (String[] row : allRows) {
                    if (row.length >= 3) {
                        for (int i = 0; i < 3; i++) {
                            try {
                                double value = Double.parseDouble(row[row.length - 3 + i].trim());
                                columnData.get(i).add(value);
                            } catch (NumberFormatException e) {
                                columnData.get(i).add(0.0);
                            }
                        }
                    }
                }

                // 统计每一列中1的数量
                int[] columnOnesCount = new int[3];
                for (int i = 0; i < columnData.size(); i++) {
                    for (Double value : columnData.get(i)) {
                        if (value == 1.0) {
                            columnOnesCount[i]++;
                        }
                    }
                }

                // 找到1最多的列
                int maxOnesColumn = 0;
                int maxOnes = columnOnesCount[0];
                for (int i = 1; i < 3; i++) {
                    if (columnOnesCount[i] > maxOnes) {
                        maxOnes = columnOnesCount[i];
                        maxOnesColumn = i;
                    }
                }

                // 返回结果
                Map<String, Object> result = new HashMap<>();
                result.put("stressLevel", maxOnesColumn);
                result.put("columnCounts", columnOnesCount);
                result.put("columnData", columnData);
                
                // 根据新的状态定义设置描述
                switch (maxOnesColumn) {
                    case 0:
                        result.put("description", "未定义/瞬态");
                        break;
                    case 1:
                        result.put("description", "基线状态");
                        break;
                    case 2:
                        result.put("description", "压力状态");
                        break;
                    default:
                        result.put("description", "未知状态");
                }

                return ResponseEntity.ok(Result.success(result));
            }

        } catch (IOException | CsvException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                .body(Result.fail("分析失败: " + e.getMessage()));
        }
    }
}
