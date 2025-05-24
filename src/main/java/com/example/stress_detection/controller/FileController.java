package com.example.stress_detection.controller;

import com.example.stress_detection.entity.FileInfo;
import com.example.stress_detection.result.Result;
import com.example.stress_detection.service.FileInfoService;
import com.example.stress_detection.vo.FileInfoVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@RestController
@RequestMapping("/files")
public class FileController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private FileInfoService fileInfoService;


    @PostMapping("/upload")
    public Result<?> uploadFile(
            @RequestParam("file") MultipartFile[] files,
            @RequestParam(value = "fileName", required = false) String fileNameOverride) {

        if (files == null || files.length == 0) {
            return Result.fail("上传失败，没有文件");
        }

        List<FileInfoVO> voList = new ArrayList<>();
        Map<String, List<MultipartFile>> folderFiles = new HashMap<>();

        try {
            // 首先对文件进行分类，识别文件夹
            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;
                
                String originalFileName = file.getOriginalFilename();
                if (originalFileName.contains("/")) {
                    // 这是文件夹中的文件
                    String folderName = originalFileName.split("/")[0];
                    folderFiles.computeIfAbsent(folderName, k -> new ArrayList<>()).add(file);
                } else {
                    // 这是单个文件
                    String safeFileName = fileNameOverride != null && !fileNameOverride.trim().isEmpty() 
                        ? fileNameOverride 
                        : originalFileName;

                    // 确保上传目录存在
                    File uploadDirFile = new File(uploadDir);
                    if (!uploadDirFile.exists()) {
                        uploadDirFile.mkdirs();
                    }

                    File dest = new File(uploadDirFile, safeFileName);
                    file.transferTo(dest);

                    FileInfoVO vo = new FileInfoVO();
                    vo.setFileId(System.currentTimeMillis());
                    vo.setFileName(safeFileName);
                    vo.setUploadTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    vo.setFilePath(dest.getAbsolutePath());
                    voList.add(vo);

                    // 写入数据库
                    FileInfo fileInfo = new FileInfo();
                    fileInfo.setFileName(safeFileName);
                    fileInfo.setFilePath(dest.getAbsolutePath());
                    fileInfo.setUploadTime(LocalDateTime.now());
                    fileInfo.setUser_id(1L);
                    fileInfoService.save(fileInfo);
                }
            }

            // 处理文件夹
            for (Map.Entry<String, List<MultipartFile>> entry : folderFiles.entrySet()) {
                String folderName = entry.getKey();
                List<MultipartFile> folderContents = entry.getValue();

                // 确保上传目录存在
                File uploadDirFile = new File(uploadDir);
                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                }

                // 创建文件夹
                File folder = new File(uploadDirFile, folderName);
                folder.mkdirs();

                // 保存文件夹中的所有文件
                for (MultipartFile file : folderContents) {
                    String originalFileName = file.getOriginalFilename();
                    File dest = new File(uploadDirFile, originalFileName);
                    dest.getParentFile().mkdirs();
                    file.transferTo(dest);
                }

                // 只添加一次文件夹信息
                FileInfoVO vo = new FileInfoVO();
                vo.setFileId(System.currentTimeMillis());
                vo.setFileName(folderName);
                vo.setUploadTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                vo.setFilePath(folder.getAbsolutePath());
                voList.add(vo);

                // 写入数据库
                FileInfo fileInfo = new FileInfo();
                fileInfo.setFileName(folderName);
                fileInfo.setFilePath(folder.getAbsolutePath());
                fileInfo.setUploadTime(LocalDateTime.now());
                fileInfo.setUser_id(1L);
                fileInfoService.save(fileInfo);
            }

            // 如果只上传一个文件/文件夹，返回单个 VO
            if (voList.size() == 1) {
                return Result.success(voList.get(0));
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put("count", voList.size());
                map.put("files", voList);
                return Result.success(map);
            }

        } catch (IOException e) {
            return Result.fail("上传失败：" + e.getMessage());
        }
    }


    @GetMapping("")
    @ApiOperation("查询已经上传的文件")
    public Result<Map<String, Object>> getUploadedFiles(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        try {
            // 使用 MyBatis-Plus 的分页查询
            Page<FileInfo> pageParam = new Page<>(page, pageSize);
            Page<FileInfo> fileInfoPage = fileInfoService.page(pageParam);
            
            List<FileInfoVO> fileInfoList = fileInfoPage.getRecords().stream()
                .map(fileInfo -> {
                    FileInfoVO vo = new FileInfoVO();
                    vo.setFileId(fileInfo.getFileId());
                    vo.setFileName(fileInfo.getFileName());
                    vo.setUploadTime(fileInfo.getUploadTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    vo.setFilePath(fileInfo.getFilePath());
                    return vo;
                })
                .collect(Collectors.toList());

            // 构建返回的结果结构
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("total", fileInfoPage.getTotal());
            responseData.put("items", fileInfoList);

            return Result.success(responseData);
        } catch (Exception e) {
            return Result.fail("获取文件列表失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{fileId}")
    @ApiOperation("删除已上传的文件")
    public Result<Void> deleteFile(@PathVariable String fileId) {
        try {
            FileInfo fileInfo = fileInfoService.getById(fileId);
            String fileName = fileInfo.getFileName();
            File file = new File(uploadDir + File.separator + fileName);
            System.out.println(file);
            if (file.exists()) {
                boolean deleted = file.delete();
                boolean b = fileInfoService.removeById(fileId);
                if (deleted && b) {
                    return Result.success(null);
                } else {
                    return Result.fail("删除失败");
                }
            } else {
                return Result.fail("文件不存在");
            }
        } catch (Exception e) {
            return Result.fail("删除文件失败：" + e.getMessage());
        }
    }

    @GetMapping("/download/{fileId}")
    @ApiOperation("下载文件")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        try {
            FileInfo fileById = fileInfoService.getById(fileId);
            String fileName = fileById.getFileName();
            System.out.println("开始下载文件: " + fileName);
            System.out.println("上传目录: " + uploadDir);
            
            // 构建文件路径
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
            System.out.println("完整文件路径: " + filePath);
            
            // 检查路径是否在上传目录内
            if (!filePath.startsWith(Paths.get(uploadDir))) {
                System.out.println("非法的文件路径访问");
                return ResponseEntity.badRequest().build();
            }

            // 从数据库查询文件信息
            FileInfo fileInfo = fileInfoService.lambdaQuery()
                .eq(FileInfo::getFileName, fileName)
                .one();
                
            if (fileInfo == null) {
                System.out.println("数据库中未找到文件记录");
                return ResponseEntity.notFound().build();
            }

            File file = new File(fileInfo.getFilePath());
            if (!file.exists() || !file.isFile()) {
                System.out.println("文件不存在: " + fileInfo.getFilePath());
                return ResponseEntity.notFound().build();
            }

            // 创建资源
            Resource resource = new UrlResource(file.toURI());
            System.out.println("resource URI= " + resource.getURI());
            System.out.println("resource URL= " + resource.getURL());
            System.out.println("resource fileName= " + resource.getFilename());
            System.out.println("resource File= " + resource.getFile());
            System.out.println("resource Description= " + resource.getDescription());
            System.out.println("resource exist?= " + resource.exists());
            
            // 设置响应头
            String contentType = "application/octet-stream";
            String headerValue = "attachment; filename=\"" + fileName + "\"";

            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);

        } catch (Exception e) {
            System.out.println("下载文件时发生错误: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
