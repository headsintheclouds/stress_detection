package com.example.stress_detection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.stress_detection.entity.File;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper extends BaseMapper<File> {
}
