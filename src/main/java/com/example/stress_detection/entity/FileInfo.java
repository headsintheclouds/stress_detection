package com.example.stress_detection.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;


@TableName("Files")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {

    @TableId(value = "file_id", type = IdType.AUTO)
    private Long fileId;

    @TableField("file_name")
    private String fileName;

    @TableField("file_path")
    private String filePath;

    @TableField("upload_time")
    private LocalDateTime uploadTime;

    @TableField("user_id")
    private Long user_id;

}