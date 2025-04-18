package com.example.stress_detection.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.stress_detection.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {


    User selectByUsername(String userName);


}
