package com.education.studentprofile.repository.mysql;

import com.education.studentprofile.entity.mysql.ProfileRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRecordRepository extends JpaRepository<ProfileRecord, Long> {

    Optional<ProfileRecord> findTopByStudentNoOrderByCreateTimeDesc(String studentNo);
}
