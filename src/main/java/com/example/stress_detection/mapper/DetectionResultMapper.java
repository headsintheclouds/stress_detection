package com.example.stress_detection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.stress_detection.entity.DetectionResult;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DetectionResultMapper extends BaseMapper<DetectionResult> {
}
