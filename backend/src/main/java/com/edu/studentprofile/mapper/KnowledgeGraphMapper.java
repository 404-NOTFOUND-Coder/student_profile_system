package com.edu.studentprofile.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.studentprofile.entity.KnowledgeGraph;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 知识图谱Mapper
 */
@Mapper
public interface KnowledgeGraphMapper extends BaseMapper<KnowledgeGraph> {

    /**
     * 根据学生学号获取其静态关系
     */
    @Select("SELECT * FROM knowledge_graph WHERE head_id = #{studentNo} AND relation_type = 'static_relation' AND deleted = 0")
    List<KnowledgeGraph> findStaticRelationsByStudentId(@Param("studentNo") String studentNo);

    /**
     * 根据学生学号获取其动态关系
     */
    @Select("SELECT * FROM knowledge_graph WHERE head_id = #{studentNo} AND relation_type = 'dynamic_relation' AND deleted = 0 ORDER BY event_time DESC")
    List<KnowledgeGraph> findDynamicRelationsByStudentId(@Param("studentNo") String studentNo);

    /**
     * 根据关系类型获取实体列表
     */
    @Select("SELECT * FROM knowledge_graph WHERE relation = #{relation} AND deleted = 0")
    List<KnowledgeGraph> findByRelation(@Param("relation") String relation);

    /**
     * 获取某时间范围内的活动
     */
    @Select("SELECT * FROM knowledge_graph WHERE relation_type = 'dynamic_relation' AND time_dimension LIKE CONCAT(#{year}, '%') AND deleted = 0")
    List<KnowledgeGraph> findActivitiesByYear(@Param("year") String year);

    /**
     * 统计学生的活动数量
     */
    @Select("SELECT COUNT(*) FROM knowledge_graph WHERE head_id = #{studentNo} AND relation_type = 'dynamic_relation' AND deleted = 0")
    Integer countActivitiesByStudentId(@Param("studentNo") String studentNo);
}
