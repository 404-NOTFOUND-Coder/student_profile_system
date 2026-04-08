package com.education.studentprofile.service;

import com.education.studentprofile.entity.mysql.Student;
import com.education.studentprofile.repository.mysql.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentByNo(String studentNo) {
        return studentRepository.findByStudentNo(studentNo);
    }

    public List<Student> getStudentsByCollege(String college) {
        return studentRepository.findByCollege(college);
    }

    public List<Student> getStudentsByEnrollmentYear(Integer year) {
        return studentRepository.findByEnrollmentYear(year);
    }

    public List<Student> searchStudents(String college, String major, String studentNo) {
        if (college != null && major != null) {
            return studentRepository.findByCollegeAndMajor(college, major);
        } else if (college != null) {
            return studentRepository.findByCollege(college);
        } else if (studentNo != null && !studentNo.isEmpty()) {
            return studentRepository.findAll().stream()
                    .filter(s -> s.getStudentNo().contains(studentNo))
                    .toList();
        }
        return studentRepository.findAll();
    }

    public Student saveStudent(Student student) {
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        return studentRepository.save(student);
    }

    public Student updateStudent(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        return studentRepository.save(student);
    }

    public void deleteStudent(String studentNo) {
        studentRepository.deleteById(studentNo);
    }

    public Long getActiveStudentCount() {
        return studentRepository.countActiveStudents();
    }

    public List<Student> getStudentsByProfileType(String profileType) {
        return studentRepository.findByProfileType(profileType);
    }
}
