package com.example.stress_detection.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfoVO {

    private Long userId;
    private String userName;
    private String email;
    private String phone;
}

