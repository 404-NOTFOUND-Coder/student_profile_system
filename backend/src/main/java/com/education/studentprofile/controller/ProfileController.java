package com.education.studentprofile.controller;

import com.education.studentprofile.service.ProfileGenerateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProfileController {

    private final ProfileGenerateService profileGenerateService;

    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateProfile(@RequestBody Map<String, String> request) {
        String studentNo = request.get("studentNo");
        if (studentNo == null || studentNo.isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("code", 400);
            error.put("message", "学号不能为空");
            return ResponseEntity.badRequest().body(error);
        }

        String report = profileGenerateService.generateProfileReport(studentNo);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", report);
        return ResponseEntity.ok(result);
    }
}
