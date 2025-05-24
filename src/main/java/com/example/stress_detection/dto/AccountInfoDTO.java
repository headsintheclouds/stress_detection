package com.example.stress_detection.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("修改信息请求体")
public class AccountInfoDTO {
    private String username;
    private String email;
    private String phone;

}
