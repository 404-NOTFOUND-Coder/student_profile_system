package com.education.studentprofile.controller;

import com.education.studentprofile.entity.mysql.Warning;
import com.education.studentprofile.service.WarningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/warnings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class WarningController {

    private final WarningService warningService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllWarnings() {
        List<Warning> warnings = warningService.getAllWarnings();
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", warnings);
        result.put("total", warnings.size());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/student/{studentNo}")
    public ResponseEntity<Map<String, Object>> getWarningsByStudent(@PathVariable String studentNo) {
        List<Warning> warnings = warningService.getWarningsByStudentNo(studentNo);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", warnings);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/college/{college}")
    public ResponseEntity<Map<String, Object>> getWarningsByCollege(@PathVariable String college) {
        List<Warning> warnings = warningService.getWarningsByCollege(college);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", warnings);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/type/{warningType}")
    public ResponseEntity<Map<String, Object>> getWarningsByType(@PathVariable String warningType) {
        List<Warning> warnings = warningService.getWarningsByType(warningType);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", warnings);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/level/{level}")
    public ResponseEntity<Map<String, Object>> getWarningsByLevel(@PathVariable Integer level) {
        List<Warning> warnings = warningService.getWarningsByLevel(level);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", warnings);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createWarning(@RequestBody Warning warning) {
        Warning saved = warningService.saveWarning(warning);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "创建成功");
        result.put("data", saved);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteWarning(@PathVariable Long id) {
        warningService.deleteWarning(id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "删除成功");
        return ResponseEntity.ok(result);
    }
}
