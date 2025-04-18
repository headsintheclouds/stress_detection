package com.example.stress_detection.service;


import com.example.stress_detection.entity.User;
import com.example.stress_detection.dto.UserLoginDTO;
import org.springframework.stereotype.Service;

public interface UserService {

    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    User login(UserLoginDTO userLoginDTO);


}
