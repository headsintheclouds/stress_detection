package com.example.stress_detection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.stress_detection.entity.FileInfo;
import com.example.stress_detection.mapper.FileInfoMapper;
import com.example.stress_detection.service.FileInfoService;
import org.springframework.stereotype.Service;

@Service
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo> implements FileInfoService {

}
