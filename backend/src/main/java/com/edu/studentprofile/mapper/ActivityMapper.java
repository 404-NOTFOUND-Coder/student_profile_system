package com.edu.studentprofile.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.studentprofile.entity.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 活动Mapper
 */
@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {

    /**
     * 获取进行中的活动
     */
    @Select("SELECT * FROM activity WHERE status = 'published' AND deleted = 0 ORDER BY activity_start_time ASC")
    IPage<Activity> findOngoingActivities(Page<?> page);

    /**
     * 获取即将开始的活动
     */
    @Select("SELECT * FROM activity WHERE status = 'published' AND register_start_time <= NOW() AND register_end_time >= NOW() AND deleted = 0 ORDER BY register_end_time ASC")
    IPage<Activity> findUpcomingActivities(Page<?> page);

    /**
     * 根据分类获取活动
     */
    @Select("SELECT * FROM activity WHERE category = #{category} AND status = 'published' AND deleted = 0 ORDER BY activity_start_time DESC")
    IPage<Activity> findByCategory(Page<?> page, @Param("category") String category);

    /**
     * 根据级别获取活动
     */
    @Select("SELECT * FROM activity WHERE level = #{level} AND status = 'published' AND deleted = 0 ORDER BY activity_start_time DESC")
    IPage<Activity> findByLevel(Page<?> page, @Param("level") Integer level);

    /**
     * 获取可推荐给特定画像类型的活动
     */
    @Select("SELECT * FROM activity WHERE status = 'published' AND enable_recommend = 1 AND deleted = 0 ORDER BY recommend_weight DESC")
    IPage<Activity> findRecommendableActivities(Page<?> page);
}
