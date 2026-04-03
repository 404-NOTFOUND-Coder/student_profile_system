package com.edu.studentprofile.controller;

import com.edu.studentprofile.entity.KnowledgeGraph;
import com.edu.studentprofile.service.KnowledgeGraphService;
import com.edu.studentprofile.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 知识图谱控制器
 */
@RestController
@RequestMapping("/api/knowledge-graph")
@CrossOrigin
public class KnowledgeGraphController {

    @Autowired
    private KnowledgeGraphService knowledgeGraphService;

    /**
     * 获取学生的知识图谱
     */
    @GetMapping("/student/{studentNo}")
    public R getStudentKnowledgeGraph(@PathVariable String studentNo) {
        Map<String, Object> graph = knowledgeGraphService.getStudentKnowledgeGraph(studentNo);
        return R.ok(graph);
    }

    /**
     * 获取学生的活动统计
     */
    @GetMapping("/student/{studentNo}/stats")
    public R getActivityStatistics(@PathVariable String studentNo) {
        Map<String, Object> stats = knowledgeGraphService.getActivityStatistics(studentNo);
        return R.ok(stats);
    }

    /**
     * 添加静态关系
     */
    @PostMapping("/static-relation")
    public R addStaticRelation(@RequestBody KnowledgeGraph knowledgeGraph) {
        boolean result = knowledgeGraphService.addStaticRelation(
                knowledgeGraph.getHeadId(),
                knowledgeGraph.getHeadName(),
                knowledgeGraph.getRelation(),
                knowledgeGraph.getTailName()
        );
        return result ? R.ok() : R.fail("添加失败");
    }

    /**
     * 添加动态关系
     */
    @PostMapping("/dynamic-relation")
    public R addDynamicRelation(@RequestBody KnowledgeGraph knowledgeGraph) {
        boolean result = knowledgeGraphService.addDynamicRelation(
                knowledgeGraph.getHeadId(),
                knowledgeGraph.getHeadName(),
                knowledgeGraph.getRelation(),
                knowledgeGraph.getSubRelation(),
                knowledgeGraph.getTailName(),
                knowledgeGraph.getTimeDimension(),
                knowledgeGraph.getActivityLevel(),
                knowledgeGraph.getAwardLevel()
        );
        return result ? R.ok() : R.fail("添加失败");
    }

    /**
     * 获取关系列表
     */
    @GetMapping("/relations")
    public R getRelations() {
        return R.ok(Map.of(
                "staticRelations", List.of(
                        Map.of("name", "隶属院系", "description", "学生所属院系"),
                        Map.of("name", "隶属专业", "description", "学生所属专业"),
                        Map.of("name", "性别", "description", "学生性别"),
                        Map.of("name", "年级", "description", "学生年级"),
                        Map.of("name", "班级", "description", "学生班级"),
                        Map.of("name", "来自于", "description", "学生生源地")
                ),
                "dynamicRelations", List.of(
                        Map.of("name", "比赛获奖", "subCategories", List.of("创新创业", "科技创新", "劳动实践", "思想成长", "体育活动", "文化艺术", "心理成长", "学术研究")),
                        Map.of("name", "培训经历", "subCategories", List.of("技能培训", "语言培训", "专业培训")),
                        Map.of("name", "工作经历", "subCategories", List.of("实习", "兼职", "勤工俭学")),
                        Map.of("name", "学生活动", "subCategories", List.of("社团活动", "班级活动", "志愿服务")),
                        Map.of("name", "个人荣誉", "subCategories", List.of("三好学生", "优秀干部", "奖学金")),
                        Map.of("name", "资格认证", "subCategories", List.of("专业证书", "技能证书", "语言证书"))
                ),
                "activityLevels", List.of("院级", "校级", "省部级", "国家级", "国际级"),
                "awardLevels", List.of("特等奖", "一等奖", "二等奖", "三等奖", "优秀奖", "参与奖")
        ));
    }

    /**
     * 获取所有知识图谱数据（用于可视化）
     */
    @GetMapping("/all")
    public R getAllGraphData(
            @RequestParam(required = false) String relationType,
            @RequestParam(required = false) String category) {
        return R.ok(Map.of(
                "nodes", List.of(),
                "links", List.of()
        ));
    }
}
