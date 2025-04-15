package com.example.stress_detection.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.stress_detection.Entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
