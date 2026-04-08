package com.education.studentprofile.repository.mysql;

import com.education.studentprofile.entity.mysql.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

    Optional<Student> findByStudentNo(String studentNo);

    List<Student> findByCollege(String college);

    List<Student> findByEnrollmentYear(Integer enrollmentYear);

    @Query("SELECT s FROM Student s WHERE s.college LIKE %:college% AND s.major LIKE %:major%")
    List<Student> findByCollegeAndMajor(@Param("college") String college, @Param("major") String major);

    @Query("SELECT COUNT(s) FROM Student s WHERE s.studentStatus = 1")
    Long countActiveStudents();

    List<Student> findByProfileType(String profileType);
}
