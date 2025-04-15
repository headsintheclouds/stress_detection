package com.example.stress_detection.entity;


import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "detection_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetectionResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resultId;

    @ManyToOne
    @JoinColumn(name = "file_id", nullable = false)
    private File file; // Foreign key to the analyzed file

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Foreign key to the user who performed the analysis

    @Column(nullable = false)
    private Double score;

    @Column
    private String chartPath;

    @Column(nullable = false)
    private LocalDateTime detectTime;

    // You might add other details about the analysis result
}