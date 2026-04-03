package com.edu.studentprofile.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.edu.studentprofile.entity.Student;
import com.edu.studentprofile.mapper.StudentMapper;
import com.edu.studentprofile.util.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Dashboard首页数据Controller
 */
@Slf4j
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 获取Dashboard统计数据
     */
    @GetMapping("/stats")
    public R<Map<String, Object>> getStats() {
        try {
            // 学生总数
            Long totalStudents = studentMapper.selectCount(null);

            // 在读学生数量
            QueryWrapper<Student> activeWrapper = new QueryWrapper<>();
            activeWrapper.eq("student_status", 1);
            Long activeStudents = studentMapper.selectCount(activeWrapper);

            // 平均GPA (从有成绩的学生中计算)
            Double avgGpa = calculateAvgGpa();

            // 预警数量（学业评分低于85分的学生）
            QueryWrapper<Student> warningWrapper = new QueryWrapper<>();
            warningWrapper.and(w -> w.lt("profile_score", 85).or().isNull("profile_score"));
            warningWrapper.eq("student_status", 1);
            Long warningCount = studentMapper.selectCount(warningWrapper);

            Map<String, Object> stats = new HashMap<>();
            stats.put("totalStudents", totalStudents != null ? totalStudents : 0);
            stats.put("activeStudents", activeStudents != null ? activeStudents : 0);
            stats.put("warningCount", warningCount != null ? warningCount : 0);
            stats.put("avgGpa", avgGpa != null ? avgGpa : 0.0);

            return R.ok(stats);
        } catch (Exception e) {
            log.error("获取统计数据失败", e);
            return R.fail("获取统计数据失败: " + e.getMessage());
        }
    }

    /**
     * 获取预警列表
     */
    @GetMapping("/warnings")
    public R<List<Map<String, Object>>> getWarnings(
            @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<Map<String, Object>> warnings = new ArrayList<>();

            // 从数据库获取有预警的学生（学业评分低于85分的学生）
            QueryWrapper<Student> wrapper = new QueryWrapper<>();
            wrapper.and(w -> w.lt("profile_score", 85).or().isNull("profile_score"));
            wrapper.eq("student_status", 1);
            wrapper.last("LIMIT " + limit);
            List<Student> students = studentMapper.selectList(wrapper);

            for (Student student : students) {
                Map<String, Object> warning = new HashMap<>();
                warning.put("studentNo", student.getStudentNo());
                warning.put("studentName", student.getName());
                warning.put("college", student.getCollege());
                warning.put("major", student.getMajor());
                warning.put("warningType", "成绩预警");
                // 根据分数判断等级
                if (student.getProfileScore() == null) {
                    warning.put("level", 3);
                    warning.put("levelName", "严重");
                    warning.put("warningMessage", "暂无学业评分，需要进行学业评估");
                } else if (student.getProfileScore() < 80) {
                    warning.put("level", 3);
                    warning.put("levelName", "严重");
                    warning.put("warningMessage", "学业评分偏低，需要重点关注");
                } else if (student.getProfileScore() < 85) {
                    warning.put("level", 2);
                    warning.put("levelName", "较重");
                    warning.put("warningMessage", "学业评分偏低，建议加强学业辅导");
                } else {
                    warning.put("level", 1);
                    warning.put("levelName", "一般");
                    warning.put("warningMessage", "学业评分有提升空间");
                }
                warning.put("createTime", new Date());
                warnings.add(warning);
            }

            return R.ok(warnings);
        } catch (Exception e) {
            log.error("获取预警列表失败", e);
            return R.fail("获取预警列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取画像示例列表
     */
    @GetMapping("/profile-examples")
    public R<List<Map<String, Object>>> getProfileExamples(
            @RequestParam(defaultValue = "5") Integer limit) {
        try {
            List<Map<String, Object>> profiles = new ArrayList<>();

            // 获取评分最高的学生作为示例
            QueryWrapper<Student> wrapper = new QueryWrapper<>();
            wrapper.eq("student_status", 1);
            wrapper.isNotNull("profile_score");
            wrapper.orderByDesc("profile_score");
            wrapper.last("LIMIT " + limit);
            List<Student> students = studentMapper.selectList(wrapper);

            // 颜色映射
            String[] colors = {"#409eff", "#67c23a", "#e6a23c", "#f56c6c", "#909399"};

            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                Map<String, Object> profile = new HashMap<>();
                profile.put("studentNo", student.getStudentNo());
                profile.put("name", student.getName());
                profile.put("college", student.getCollege());
                profile.put("major", student.getMajor());

                // 根据评分判断画像类型（profile_score 是百分制，转换为4分制）
                Double score = student.getProfileScore();
                Double gpa = score != null ? Math.round((score / 25.0) * 100.0) / 100.0 : 0.0;
                String profileType;
                String abilityLevel;
                String abilityType = "";

                // 根据百分制分数判断画像类型
                if (score != null && score >= 90) {
                    profileType = "学术研究型";
                    abilityLevel = "A+";
                    abilityType = "success";
                } else if (score != null && score >= 85) {
                    profileType = "实践应用型";
                    abilityLevel = "A";
                    abilityType = "primary";
                } else if (score != null && score >= 80) {
                    profileType = "复合探索型";
                    abilityLevel = "B+";
                    abilityType = "warning";
                } else if (score != null && score >= 75) {
                    profileType = "创新创业型";
                    abilityLevel = "B";
                    abilityType = "info";
                } else {
                    profileType = "潜力发展型";
                    abilityLevel = "C";
                    abilityType = "danger";
                }

                profile.put("profileType", profileType);
                profile.put("gpa", gpa);
                profile.put("abilityLevel", abilityLevel);
                profile.put("abilityType", abilityType);
                profile.put("avatarColor", colors[i % colors.length]);

                profiles.add(profile);
            }

            return R.ok(profiles);
        } catch (Exception e) {
            log.error("获取画像示例失败", e);
            return R.fail("获取画像示例失败: " + e.getMessage());
        }
    }

    /**
     * 获取学院统计数据
     */
    @GetMapping("/college-stats")
    public R<List<Map<String, Object>>> getCollegeStats() {
        try {
            List<Map<String, Object>> collegeStats = studentMapper.selectStudentCountByCollege();
            return R.ok(collegeStats);
        } catch (Exception e) {
            log.error("获取学院统计失败", e);
            return R.fail("获取学院统计失败: " + e.getMessage());
        }
    }

    /**
     * 计算平均GPA
     */
    private Double calculateAvgGpa() {
        try {
            // 简单的平均GPA计算，实际应该从成绩表计算
            QueryWrapper<Student> wrapper = new QueryWrapper<>();
            wrapper.isNotNull("profile_score");
            wrapper.eq("student_status", 1);
            List<Student> students = studentMapper.selectList(wrapper);

            if (students.isEmpty()) {
                return 3.0;
            }

            double sum = 0;
            int count = 0;
            for (Student student : students) {
                if (student.getProfileScore() != null) {
                    // profile_score 是百分制，转换为4分制
                    sum += (student.getProfileScore() / 25.0);
                    count++;
                }
            }

            return count > 0 ? Math.round((sum / count) * 100.0) / 100.0 : 3.0;
        } catch (Exception e) {
            log.warn("计算平均GPA失败", e);
            return 3.0;
        }
    }
}
