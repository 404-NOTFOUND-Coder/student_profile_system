package com.edu.studentprofile.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.studentprofile.entity.ActivityRecommendation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 活动推荐Mapper
 */
@Mapper
public interface ActivityRecommendationMapper extends BaseMapper<ActivityRecommendation> {

    /**
     * 根据学生ID获取推荐列表
     */
    @Select("SELECT * FROM activity_recommendation WHERE student_id = #{studentId} AND deleted = 0 ORDER BY recommend_score DESC")
    List<ActivityRecommendation> findByStudentId(@Param("studentId") Long studentId);

    /**
     * 分页获取学生的推荐列表
     */
    @Select("SELECT * FROM activity_recommendation WHERE student_id = #{studentId} AND deleted = 0 ORDER BY recommend_score DESC")
    IPage<ActivityRecommendation> findPageByStudentId(Page<?> page, @Param("studentId") Long studentId);

    /**
     * 获取热门推荐
     */
    @Select("SELECT ar.* FROM activity_recommendation ar " +
           "INNER JOIN activity a ON ar.activity_id = a.id " +
           "WHERE a.status = 'published' AND ar.deleted = 0 " +
           "ORDER BY ar.recommend_score DESC LIMIT #{limit}")
    List<ActivityRecommendation> findHotRecommendations(@Param("limit") Integer limit);

    /**
     * 根据活动ID获取被推荐的学生列表
     */
    @Select("SELECT * FROM activity_recommendation WHERE activity_id = #{activityId} AND deleted = 0")
    List<ActivityRecommendation> findByActivityId(@Param("activityId") Long activityId);

    /**
     * 更新推荐状态
     */
    @Select("UPDATE activity_recommendation SET status = #{status}, update_time = NOW() WHERE id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("status") String status);
}
