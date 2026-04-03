package com.edu.studentprofile.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.studentprofile.entity.Activity;
import com.edu.studentprofile.entity.ActivityRecommendation;
import com.edu.studentprofile.service.ActivityService;
import com.edu.studentprofile.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 活动控制器
 */
@RestController
@RequestMapping("/api/activity")
@CrossOrigin
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    /**
     * 分页查询活动
     */
    @GetMapping("/page")
    public R queryPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer level,
            @RequestParam(required = false) Integer activityType,
            @RequestParam(required = false) String status) {
        Page<Activity> page = new Page<>(pageNum, pageSize);
        Activity activity = new Activity();
        activity.setCategory(category);
        activity.setLevel(level);
        activity.setActivityType(activityType);
        activity.setStatus(status);
        Page<Activity> result = activityService.queryPage(page, activity);
        return R.ok(result);
    }

    /**
     * 获取活动详情
     */
    @GetMapping("/{id}")
    public R getById(@PathVariable Long id) {
        Activity activity = activityService.getById(id);
        if (activity != null) {
            activity.setActivityTypeName(activityService.getActivityTypeName(activity.getActivityType()));
            activity.setLevelName(activityService.getLevelName(activity.getLevel()));
        }
        return R.ok(activity);
    }

    /**
     * 创建活动
     */
    @PostMapping
    public R save(@RequestBody Activity activity) {
        boolean result = activityService.save(activity);
        return result ? R.ok() : R.fail("创建失败");
    }

    /**
     * 更新活动
     */
    @PutMapping("/{id}")
    public R update(@PathVariable Long id, @RequestBody Activity activity) {
        activity.setId(id);
        boolean result = activityService.updateById(activity);
        return result ? R.ok() : R.fail("更新失败");
    }

    /**
     * 删除活动
     */
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        boolean result = activityService.removeById(id);
        return result ? R.ok() : R.fail("删除失败");
    }

    /**
     * 获取热门活动
     */
    @GetMapping("/hot")
    public R getHotActivities(@RequestParam(defaultValue = "10") Integer limit) {
        Page<Activity> page = new Page<>(1, limit);
        QueryWrapper<Activity> wrapper = new QueryWrapper<>();
        wrapper.eq("status", "published");
        wrapper.eq("deleted", 0);
        wrapper.orderByDesc("recommend_weight");
        Page<Activity> result = activityService.page(page, wrapper);
        return R.ok(result.getRecords());
    }

    /**
     * 获取即将开始的活动
     */
    @GetMapping("/upcoming")
    public R getUpcomingActivities(@RequestParam(defaultValue = "10") Integer limit) {
        Page<Activity> page = new Page<>(1, limit);
        QueryWrapper<Activity> wrapper = new QueryWrapper<>();
        wrapper.eq("status", "published");
        wrapper.eq("deleted", 0);
        wrapper.le("register_start_time", LocalDate.now());
        wrapper.ge("register_end_time", LocalDate.now());
        wrapper.orderByAsc("register_end_time");
        Page<Activity> result = activityService.page(page, wrapper);
        return R.ok(result.getRecords());
    }

    /**
     * 为学生生成推荐
     */
    @PostMapping("/recommend/{studentId}")
    public R generateRecommendations(
            @PathVariable Long studentId,
            @RequestParam String studentNo,
            @RequestParam(required = false) String profileType,
            @RequestParam(defaultValue = "10") Integer limit) {
        List<ActivityRecommendation> recommendations = activityService.generateRecommendations(
                studentId, studentNo, profileType, limit);
        return R.ok(recommendations);
    }

    /**
     * 获取学生的推荐列表
     */
    @GetMapping("/recommendations/{studentId}")
    public R getStudentRecommendations(
            @PathVariable Long studentId,
            @RequestParam(required = false) String status) {
        List<ActivityRecommendation> recommendations = activityService.getStudentRecommendations(studentId, status);
        return R.ok(recommendations);
    }

    /**
     * 更新推荐状态
     */
    @PutMapping("/recommendation/{id}")
    public R updateRecommendationStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        boolean result = activityService.updateRecommendationStatus(id, status);
        return result ? R.ok() : R.fail("更新失败");
    }

    /**
     * 获取活动分类列表
     */
    @GetMapping("/categories")
    public R getCategories() {
        return R.ok(List.of(
                java.util.Map.of("value", "比赛获奖", "label", "比赛获奖"),
                java.util.Map.of("value", "培训经历", "label", "培训经历"),
                java.util.Map.of("value", "工作经历", "label", "工作经历"),
                java.util.Map.of("value", "学生活动", "label", "学生活动"),
                java.util.Map.of("value", "个人荣誉", "label", "个人荣誉"),
                java.util.Map.of("value", "资格认证", "label", "资格认证")
        ));
    }

    /**
     * 获取活动级别列表
     */
    @GetMapping("/levels")
    public R getLevels() {
        return R.ok(List.of(
                java.util.Map.of("value", 1, "label", "院级"),
                java.util.Map.of("value", 2, "label", "校级"),
                java.util.Map.of("value", 3, "label", "省部级"),
                java.util.Map.of("value", 4, "label", "国家级"),
                java.util.Map.of("value", 5, "label", "国际级")
        ));
    }

    /**
     * 获取活动类型列表
     */
    @GetMapping("/types")
    public R getTypes() {
        return R.ok(List.of(
                java.util.Map.of("value", 1, "label", "竞赛"),
                java.util.Map.of("value", 2, "label", "科研"),
                java.util.Map.of("value", 3, "label", "社会实践"),
                java.util.Map.of("value", 4, "label", "学生工作"),
                java.util.Map.of("value", 5, "label", "志愿服务"),
                java.util.Map.of("value", 6, "label", "文体活动"),
                java.util.Map.of("value", 7, "label", "培训"),
                java.util.Map.of("value", 8, "label", "资格认证")
        ));
    }
}
