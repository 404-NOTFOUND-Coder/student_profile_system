package com.education.studentprofile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {

    // 跨模块调用
    private final com.education.studentprofile.service.StudentService studentService;
    private final com.education.studentprofile.service.WarningService warningService;
    private final com.education.studentprofile.service.ActivityService activityService;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalStudents", studentService.getAllStudents().size());
        stats.put("activeStudents", studentService.getActiveStudentCount());
        stats.put("warningCount", warningService.getAllWarnings().size());
        stats.put("activityCount", activityService.getAllActivities().size());

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", stats);
        return ResponseEntity.ok(result);
    }
}
