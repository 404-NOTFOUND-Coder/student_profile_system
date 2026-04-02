package com.edu.studentprofile.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.studentprofile.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 学生Mapper接口
 */
@Mapper
public interface StudentMapper extends BaseMapper<Student> {

    /**
     * 分页查询学生列表
     */
    IPage<Student> selectStudentPage(Page<Student> page,
                                     @Param("name") String name,
                                     @Param("college") String college,
                                     @Param("major") String major,
                                     @Param("enrollmentYear") Integer enrollmentYear,
                                     @Param("studentStatus") Integer studentStatus);

    /**
     * 根据学号查询学生详情
     */
    @Select("SELECT * FROM sys_student WHERE student_no = #{studentNo} AND deleted = 0")
    Student selectByStudentNo(@Param("studentNo") String studentNo);

    /**
     * 查询学生的课程成绩
     */
    List<com.edu.studentprofile.entity.AcademicScore> selectStudentScores(@Param("studentId") Long studentId);

    /**
     * 查询学生活动
     */
    List<com.edu.studentprofile.entity.StudentActivity> selectStudentActivities(@Param("studentId") Long studentId);

    /**
     * 批量查询学生
     */
    List<Student> selectBatchByStudentNos(@Param("studentNos") List<String> studentNos);

    /**
     * 统计各学院学生数量
     */
    List<Map<String, Object>> selectStudentCountByCollege();

    /**
     * 统计各专业学生数量
     */
    List<Map<String, Object>> selectStudentCountByMajor();
}
