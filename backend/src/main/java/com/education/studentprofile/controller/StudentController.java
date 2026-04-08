package com.education.studentprofile.controller;

import com.education.studentprofile.entity.mysql.Student;
import com.education.studentprofile.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", students);
        result.put("total", students.size());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{studentNo}")
    public ResponseEntity<Map<String, Object>> getStudentByNo(@PathVariable String studentNo) {
        return studentService.getStudentByNo(studentNo)
                .map(student -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("code", 200);
                    result.put("data", student);
                    return ResponseEntity.ok(result);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchStudents(
            @RequestParam(required = false) String college,
            @RequestParam(required = false) String major,
            @RequestParam(required = false) String studentNo) {
        List<Student> students = studentService.searchStudents(college, major, studentNo);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", students);
        result.put("total", students.size());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/college/{college}")
    public ResponseEntity<Map<String, Object>> getStudentsByCollege(@PathVariable String college) {
        List<Student> students = studentService.getStudentsByCollege(college);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", students);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/profile-type/{profileType}")
    public ResponseEntity<Map<String, Object>> getStudentsByProfileType(@PathVariable String profileType) {
        List<Student> students = studentService.getStudentsByProfileType(profileType);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", students);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createStudent(@RequestBody Student student) {
        Student saved = studentService.saveStudent(student);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "创建成功");
        result.put("data", saved);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{studentNo}")
    public ResponseEntity<Map<String, Object>> updateStudent(
            @PathVariable String studentNo,
            @RequestBody Student student) {
        student.setStudentNo(studentNo);
        Student updated = studentService.updateStudent(student);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "更新成功");
        result.put("data", updated);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{studentNo}")
    public ResponseEntity<Map<String, Object>> deleteStudent(@PathVariable String studentNo) {
        studentService.deleteStudent(studentNo);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "删除成功");
        return ResponseEntity.ok(result);
    }

    @GetMapping("/stats/active")
    public ResponseEntity<Map<String, Object>> getActiveStudentCount() {
        Long count = studentService.getActiveStudentCount();
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", count);
        return ResponseEntity.ok(result);
    }
}
