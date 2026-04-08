package com.education.studentprofile.entity.mysql;

import jakarta.persistence.*;
import lombok.Data;
import java.LocalDateTime;

@Data
@Entity
@Table(name = "activity")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "level", length = 20)
    private String level;

    @Column(name = "time", length = 20)
    private String time;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "participants", length = 2000)
    private String participants;

    @Column(name = "award", length = 100)
    private String award;

    @Column(name = "create_time")
    private LocalDateTime createTime;
}
