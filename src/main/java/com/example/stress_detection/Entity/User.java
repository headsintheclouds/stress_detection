package com.example.stress_detection.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;


@TableName("Users")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class User {

    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @TableField(value = "email")
    private String email;

    @TableField(value = "password")
    private String password;

    @TableField(value = "name")
    private String name;
    @TableField(value = "phone")
    private String phone;

    private LocalDateTime registrationTime;
    private LocalDateTime lastLoginTime;

    // Getters and setters
}
