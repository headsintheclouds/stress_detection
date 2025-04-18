package com.example.stress_detection.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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


    // Getters and setters
}
