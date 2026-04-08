package com.education.studentprofile.controller;

import com.education.studentprofile.entity.mysql.Activity;
import com.education.studentprofile.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllActivities() {
        List<Activity> activities = activityService.getAllActivities();
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", activities);
        result.put("total", activities.size());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getActivityById(@PathVariable Long id) {
        return activityService.getActivityById(id)
                .map(activity -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("code", 200);
                    result.put("data", activity);
                    return ResponseEntity.ok(result);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Map<String, Object>> getActivitiesByType(@PathVariable String type) {
        List<Activity> activities = activityService.getActivitiesByType(type);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", activities);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/level/{level}")
    public ResponseEntity<Map<String, Object>> getActivitiesByLevel(@PathVariable String level) {
        List<Activity> activities = activityService.getActivitiesByLevel(level);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", activities);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/student/{studentNo}")
    public ResponseEntity<Map<String, Object>> getActivitiesByStudent(@PathVariable String studentNo) {
        List<Activity> activities = activityService.getActivitiesByStudentNo(studentNo);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", activities);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchActivities(@RequestParam String keyword) {
        List<Activity> activities = activityService.searchActivities(keyword);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", activities);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createActivity(@RequestBody Activity activity) {
        Activity saved = activityService.saveActivity(activity);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "创建成功");
        result.put("data", saved);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateActivity(
            @PathVariable Long id,
            @RequestBody Activity activity) {
        activity.setId(id);
        Activity updated = activityService.updateActivity(activity);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "更新成功");
        result.put("data", updated);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "删除成功");
        return ResponseEntity.ok(result);
    }
}
