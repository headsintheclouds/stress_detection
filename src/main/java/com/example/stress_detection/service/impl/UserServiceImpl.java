package com.example.stress_detection.service.impl;

import com.example.stress_detection.entity.User;
import com.example.stress_detection.mapper.UserMapper;
import com.example.stress_detection.dto.UserLoginDTO;
import com.example.stress_detection.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(UserLoginDTO userLoginDTO) {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();
        User user = userMapper.selectByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("用户存在！" );
            return user;
        }

        return null;
    }
}
