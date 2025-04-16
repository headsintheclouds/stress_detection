package com.example.stress_detection.Entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;


@TableName("Files")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class File {

    @TableId(value = "file_id", type = IdType.AUTO)
    private Long fileId;

    @TableField("file_name")
    private String fileName;

    @TableField("file_path")
    private String filePath;

    @TableField("upload_time")
    private LocalDateTime uploadTime;

    @TableField("user_id")
    private Long user_id; // Foreign key to the User who uploaded the file

    // You might add other relevant fields like file size, content type, etc.
}