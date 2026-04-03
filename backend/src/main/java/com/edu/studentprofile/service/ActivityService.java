package com.edu.studentprofile.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.studentprofile.entity.Activity;
import com.edu.studentprofile.entity.ActivityRecommendation;
import com.edu.studentprofile.entity.KnowledgeGraph;
import com.edu.studentprofile.mapper.ActivityMapper;
import com.edu.studentprofile.mapper.ActivityRecommendationMapper;
import com.edu.studentprofile.mapper.KnowledgeGraphMapper;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 活动服务
 */
@Service
public class ActivityService extends ServiceImpl<ActivityMapper, Activity> {

    @Resource
    private ActivityRecommendationMapper recommendationMapper;

    @Resource
    private KnowledgeGraphMapper knowledgeGraphMapper;

    /**
     * 获取活动分类名称
     */
    public String getCategoryName(Integer category) {
        if (category == null) return "未知";
        switch (category) {
            case 1: return "比赛获奖";
            case 2: return "培训经历";
            case 3: return "工作经历";
            case 4: return "学生活动";
            case 5: return "个人荣誉";
            case 6: return "资格认证";
            case 7: return "志愿服务";
            case 8: return "文体活动";
            default: return "其他";
        }
    }

    /**
     * 获取活动级别名称
     */
    public String getLevelName(Integer level) {
        if (level == null) return "未知";
        switch (level) {
            case 1: return "院级";
            case 2: return "校级";
            case 3: return "省部级";
            case 4: return "国家级";
            case 5: return "国际级";
            default: return "其他";
        }
    }

    /**
     * 获取活动类型名称
     */
    public String getActivityTypeName(Integer type) {
        if (type == null) return "未知";
        switch (type) {
            case 1: return "竞赛";
            case 2: return "科研";
            case 3: return "社会实践";
            case 4: return "学生工作";
            case 5: return "志愿服务";
            case 6: return "文体活动";
            case 7: return "培训";
            case 8: return "资格认证";
            default: return "其他";
        }
    }

    /**
     * 分页查询活动
     */
    public Page<Activity> queryPage(Page<Activity> page, Activity activity) {
        QueryWrapper<Activity> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", 0);
        
        if (activity.getCategory() != null) {
            wrapper.eq("category", activity.getCategory());
        }
        if (activity.getLevel() != null) {
            wrapper.eq("level", activity.getLevel());
        }
        if (activity.getActivityType() != null) {
            wrapper.eq("activity_type", activity.getActivityType());
        }
        if (activity.getStatus() != null) {
            wrapper.eq("status", activity.getStatus());
        }
        
        wrapper.orderByDesc("create_time");
        return this.page(page, wrapper);
    }

    /**
     * 为学生生成活动推荐
     */
    public List<ActivityRecommendation> generateRecommendations(Long studentId, String studentNo, 
                                                                 String profileType, int limit) {
        List<ActivityRecommendation> recommendations = new ArrayList<>();
        
        // 获取学生的历史活动类型偏好
        List<KnowledgeGraph> historyActivities = knowledgeGraphMapper.findDynamicRelationsByStudentId(studentNo);
        Map<String, Long> activityPreference = historyActivities.stream()
                .collect(Collectors.groupingBy(KnowledgeGraph::getRelation, Collectors.counting()));
        
        // 获取可推荐的活动
        QueryWrapper<Activity> wrapper = new QueryWrapper<>();
        wrapper.eq("status", "published");
        wrapper.eq("enable_recommend", 1);
        wrapper.eq("deleted", 0);
        wrapper.ge("register_end_time", LocalDate.now());
        wrapper.orderByDesc("recommend_weight");
        
        List<Activity> activities = this.list(wrapper);
        
        for (Activity activity : activities) {
            double score = calculateRecommendScore(activity, profileType, activityPreference);
            
            ActivityRecommendation rec = new ActivityRecommendation();
            rec.setStudentId(studentId);
            rec.setStudentNo(studentNo);
            rec.setActivityId(activity.getId());
            rec.setActivityName(activity.getName());
            rec.setActivityType(activity.getActivityType());
            rec.setCategory(activity.getCategory());
            rec.setSubCategory(activity.getSubCategory());
            rec.setRecommendScore(score);
            rec.setRecommendReason(generateRecommendReason(activity, profileType));
            rec.setLevel(activity.getLevelName());
            rec.setOrganizer(activity.getOrganizer());
            rec.setRegisterStartTime(activity.getRegisterStartTime());
            rec.setRegisterEndTime(activity.getRegisterEndTime());
            rec.setActivityStartTime(activity.getActivityStartTime());
            rec.setActivityEndTime(activity.getActivityEndTime());
            rec.setLocation(activity.getLocation());
            rec.setReward(activity.getReward());
            rec.setStatus("pending");
            rec.setCreateTime(LocalDateTime.now());
            
            recommendations.add(rec);
        }
        
        // 按推荐分数排序并限制数量
        return recommendations.stream()
                .sorted(Comparator.comparingDouble(ActivityRecommendation::getRecommendScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * 计算推荐分数
     */
    private double calculateRecommendScore(Activity activity, String profileType, 
                                          Map<String, Long> activityPreference) {
        double score = 50.0; // 基础分数
        
        // 根据活动权重
        if (activity.getRecommendWeight() != null) {
            score += activity.getRecommendWeight() * 10;
        }
        
        // 根据画像类型匹配
        if (profileType != null) {
            switch (profileType) {
                case "学术研究型":
                    if (Integer.valueOf(1).equals(activity.getRecommendToResearchType())) {
                        score += 20;
                    }
                    break;
                case "创新创业型":
                    if (Integer.valueOf(1).equals(activity.getRecommendToStartupType())) {
                        score += 20;
                    }
                    break;
                case "实践应用型":
                    if (activity.getCategory() != null &&
                        (activity.getCategory().equals("比赛获奖") || activity.getCategory().equals("社会实践"))) {
                        score += 15;
                    }
                    break;
            }
        }
        
        // 根据历史偏好
        String category = activity.getCategory();
        if (category != null && activityPreference.containsKey(category)) {
            score += activityPreference.get(category) * 2;
        }
        
        // 根据级别加成
        if (activity.getLevel() != null) {
            score += activity.getLevel() * 3;
        }
        
        // 根据报名剩余时间
        if (activity.getRegisterEndTime() != null) {
            long daysLeft = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), activity.getRegisterEndTime());
            if (daysLeft <= 7 && daysLeft > 0) {
                score += 10; // 即将截止，增加紧迫感
            }
        }
        
        return Math.min(score, 100.0);
    }

    /**
     * 生成推荐理由
     */
    private String generateRecommendReason(Activity activity, String profileType) {
        List<String> reasons = new ArrayList<>();
        
        if (profileType != null) {
            switch (profileType) {
                case "学术研究型":
                    reasons.add("符合您学术研究型的画像特点");
                    break;
                case "实践应用型":
                    reasons.add("有助于提升您的实践能力");
                    break;
                case "创新创业型":
                    reasons.add("契合您的创新创业潜力");
                    break;
            }
        }
        
        if (activity.getLevel() != null) {
            reasons.add(getLevelName(activity.getLevel()) + "活动，含金量高");
        }
        
        if (reasons.isEmpty()) {
            reasons.add("推荐您参加此类活动");
        }
        
        return String.join("，", reasons);
    }

    /**
     * 保存推荐结果
     */
    public boolean saveRecommendations(List<ActivityRecommendation> recommendations) {
        if (recommendations == null || recommendations.isEmpty()) {
            return true;
        }
        for (ActivityRecommendation rec : recommendations) {
            recommendationMapper.insert(rec);
        }
        return true;
    }

    /**
     * 获取学生的推荐列表
     */
    public List<ActivityRecommendation> getStudentRecommendations(Long studentId, String status) {
        QueryWrapper<ActivityRecommendation> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id", studentId);
        wrapper.eq("deleted", 0);
        
        if (status != null) {
            wrapper.eq("status", status);
        }
        
        wrapper.orderByDesc("recommend_score");
        return recommendationMapper.selectList(wrapper);
    }

    /**
     * 更新推荐状态
     */
    public boolean updateRecommendationStatus(Long id, String status) {
        ActivityRecommendation rec = new ActivityRecommendation();
        rec.setId(id);
        rec.setStatus(status);
        rec.setUpdateTime(LocalDateTime.now());
        return recommendationMapper.updateById(rec) > 0;
    }
}
