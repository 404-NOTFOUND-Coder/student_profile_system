package com.education.studentprofile.entity.mysql;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "profile_record")
public class ProfileRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "student_no", length = 20)
    private String studentNo;

    @Column(name = "report_content", columnDefinition = "TEXT")
    private String reportContent;

    @Column(name = "profile_type", length = 50)
    private String profileType;

    @Column(name = "knowledge_entropy")
    private Double knowledgeEntropy;

    @Column(name = "graph_edit_distance")
    private Double graphEditDistance;

    @Column(name = "create_time")
    private LocalDateTime createTime;
}
