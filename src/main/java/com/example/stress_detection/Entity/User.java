package com.example.stress_detection.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;


@TableName("users")
@Data
@NoArgsConstructor
@AllArgsConstructor

// TODO ： 需要使用MP注解重写

public class User {

    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;
    private String phone;
    private LocalDateTime registrationTime;
    private LocalDateTime lastLoginTime;

    // Getters and setters
}
