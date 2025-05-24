package com.example.stress_detection.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileInfoVO {
    private Long fileId;
    private String fileName;
    private String uploadTime;
    private String filePath;
}
