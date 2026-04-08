package com.education.studentprofile.entity.mysql;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "warning")
public class Warning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "student_no", length = 20)
    private String studentNo;

    @Column(name = "college", length = 200)
    private String college;

    @Column(name = "major", length = 100)
    private String major;

    @Column(name = "warning_type", length = 50)
    private String warningType;

    @Column(name = "level")
    private Integer level;

    @Column(name = "level_name", length = 20)
    private String levelName;

    @Column(name = "warning_message", length = 500)
    private String warningMessage;

    @Column(name = "suggest_action", length = 500)
    private String suggestAction;

    @Column(name = "create_time")
    private LocalDateTime createTime;
}
