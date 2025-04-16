package com.example.stress_detection.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

@TableName("detection_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetectionResult {

    @TableId(value = "result_id", type = IdType.AUTO)
    private Long resultId;

    @TableField(value = "file_id")
    private Long fileId;

    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "score")
    private Double score;

    @TableField(value = "chart_path")
    private String chartPath;

    @TableField(value = "detect_time")
    private LocalDateTime detectTime;
}
