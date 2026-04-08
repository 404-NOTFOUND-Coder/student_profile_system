package com.education.studentprofile.repository.mysql;

import com.education.studentprofile.entity.mysql.Warning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarningRepository extends JpaRepository<Warning, Long> {

    List<Warning> findByStudentNo(String studentNo);

    List<Warning> findByCollege(String college);

    List<Warning> findByWarningType(String warningType);

    List<Warning> findByLevel(Integer level);
}
