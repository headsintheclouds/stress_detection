package com.example.stress_detection.Entity;

import com.baomidou.mybatisplus.annotation.*;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@TableName("files")
@Data
@NoArgsConstructor
@AllArgsConstructor

// TODO ： 需要使用MP注解重写
public class File {

    @TableId(value = "file_id", type = IdType.AUTO)
    private Long fileId;

    @TableField("")
    private String fileName;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private LocalDateTime uploadTime;

    @TableField("user_id")
    private Long user_id; // Foreign key to the User who uploaded the file

    // You might add other relevant fields like file size, content type, etc.
}