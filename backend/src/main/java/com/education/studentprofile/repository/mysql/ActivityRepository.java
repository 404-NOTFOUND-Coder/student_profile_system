package com.education.studentprofile.repository.mysql;

import com.education.studentprofile.entity.mysql.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findByType(String type);

    List<Activity> findByLevel(String level);

    @Query("SELECT a FROM Activity a WHERE a.participants LIKE %:studentNo%")
    List<Activity> findByParticipant(@Param("studentNo") String studentNo);

    @Query("SELECT a FROM Activity a WHERE a.name LIKE %:keyword%")
    List<Activity> searchByName(@Param("keyword") String keyword);
}
