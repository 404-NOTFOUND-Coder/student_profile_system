package com.edu.studentprofile.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.studentprofile.entity.Student;
import com.edu.studentprofile.mapper.StudentMapper;
import com.edu.studentprofile.util.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 学生画像Controller
 */
@Slf4j
@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StudentProfileController {

    private final StudentMapper studentMapper;

    // ==================== 学生档案管理 ====================

    /**
     * 分页查询学生列表
     */
    @GetMapping("/list")
    public R<IPage<Student>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String college,
            @RequestParam(required = false) String major,
            @RequestParam(required = false) Integer enrollmentYear,
            @RequestParam(required = false) Integer studentStatus) {

        Page<Student> page = new Page<>(pageNum, pageSize);
        IPage<Student> result = studentMapper.selectStudentPage(
            page, name, college, major, enrollmentYear, studentStatus);

        return R.ok(result);
    }

    /**
     * 根据学号获取学生详情
     */
    @GetMapping("/{studentNo}")
    public R<Student> getByStudentNo(@PathVariable String studentNo) {
        Student student = studentMapper.selectByStudentNo(studentNo);
        if (student == null) {
            return R.fail("学生不存在");
        }
        return R.ok(student);
    }

    /**
     * 创建学生档案
     */
    @PostMapping
    public R<String> create(@RequestBody Student student) {
        return R.ok("创建成功");
    }

    /**
     * 更新学生档案
     */
    @PutMapping("/{id}")
    public R<String> update(@PathVariable Long id, @RequestBody Student student) {
        return R.ok("更新成功");
    }

    /**
     * 删除学生档案
     */
    @DeleteMapping("/{id}")
    public R<String> delete(@PathVariable Long id) {
        return R.ok("删除成功");
    }

    // ==================== 学生画像接口 ====================

    /**
     * 获取学生完整画像
     */
    @GetMapping("/{studentNo}/profile")
    public R<Student> getProfile(@PathVariable String studentNo) {
        try {
            Student student = studentMapper.selectByStudentNo(studentNo);
            if (student == null) {
                return R.fail("学生画像不存在");
            }
            return R.ok(student);
        } catch (Exception e) {
            log.error("获取学生画像失败: studentNo={}", studentNo, e);
            return R.fail("获取画像失败: " + e.getMessage());
        }
    }

    // ==================== 学业分析接口 ====================

    /**
     * 学业预警分析
     */
    @GetMapping("/{studentNo}/warning")
    public R<List<Object>> academicWarning(@PathVariable String studentNo) {
        return R.ok(List.of());
    }

    /**
     * 学生相似度分析
     */
    @GetMapping("/{studentNo}/similar")
    public R<List<Object>> findSimilarStudents(
            @PathVariable String studentNo,
            @RequestParam(defaultValue = "5") Integer limit) {
        return R.ok(List.of());
    }

    // ==================== 批量操作接口 ====================

    /**
     * 批量查询学生档案
     */
    @PostMapping("/batch")
    public R<List<Student>> batchGetStudents(@RequestBody List<String> studentNos) {
        List<Student> students = studentMapper.selectBatchByStudentNos(studentNos);
        return R.ok(students);
    }

    // ==================== 统计分析接口 ====================

    /**
     * 各学院学生数量统计
     */
    @GetMapping("/stats/college")
    public R<List<Map<String, Object>>> statsByCollege() {
        List<Map<String, Object>> stats = studentMapper.selectStudentCountByCollege();
        return R.ok(stats);
    }

    /**
     * 各专业学生数量统计
     */
    @GetMapping("/stats/major")
    public R<List<Map<String, Object>>> statsByMajor() {
        List<Map<String, Object>> stats = studentMapper.selectStudentCountByMajor();
        return R.ok(stats);
    }
}
