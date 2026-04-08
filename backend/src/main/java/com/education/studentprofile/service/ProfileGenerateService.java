package com.education.studentprofile.service;

import com.education.studentprofile.entity.mysql.*;
import com.education.studentprofile.repository.mysql.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileGenerateService {

    private final StudentRepository studentRepository;
    private final ActivityRepository activityRepository;
    private final ProfileRecordRepository profileRecordRepository;

    public String generateProfileReport(String studentNo) {
        Student student = studentRepository.findByStudentNo(studentNo).orElse(null);
        if (student == null) {
            return "学生不存在";
        }

        List<Activity> activities = activityRepository.findByParticipant(studentNo);

        int activityCount = activities.size();
        long nationalLevel = activities.stream()
                .filter(a -> "国家级".equals(a.getLevel()) || "国际级".equals(a.getLevel()))
                .count();
        int volunteerHours = (int) activities.stream()
                .filter(a -> "志愿服务".equals(a.getType()))
                .count() * 8;

        String profileType = student.getProfileType() != null ? student.getProfileType() : "复合探索型";
        double knowledgeEntropy = student.getKnowledgeEntropy() != null ? student.getKnowledgeEntropy() : 3.5;
        double graphEditDistance = student.getGraphEditDistance() != null ? student.getGraphEditDistance() : 5.0;

        StringBuilder report = new StringBuilder();
        report.append("你好，我是教育诊断专家 AI。基于你提供的多模态数据，我对该学生（ID: ").append(studentNo).append("）进行了深度画像分析，报告如下：\n\n");

        // 1. 事实归纳
        report.append("### 1. 事实归纳：").append(profileType).append("轨迹\n\n");
        report.append(getTypeDescription(profileType)).append("\n");
        report.append("* **组织经历：** 积极参与各类学生活动，具备一定的团队协作能力。\n");
        report.append("* **综合素质：** 参与").append(activityCount).append("项活动，其中").append(nationalLevel).append("项为国家级以上，展现出全面的发展潜力。\n");
        report.append("* **实践基础：** 累计志愿服务").append(volunteerHours).append("小时，体现了良好的社会责任感。\n\n");

        // 2. 指标诊断
        report.append("### 2. 指标诊断：知识结构分析\n");
        report.append("* **知识结构熵 H(S)=").append(String.format("%.1f", knowledgeEntropy)).append("：**\n");
        report.append(knowledgeEntropy > 3.5 ?
                "该生知识结构呈现高度发散状态。这表明其兴趣点广泛，倾向于跨学科、跨领域的知识摄取。\n" :
                "该生知识结构相对集中，具有较好的专业深度。\n");
        report.append("* **学习路径偏离度 GED=").append(String.format("%.2f", graphEditDistance)).append("：**\n");
        report.append("结合其丰富的社会实践与组织经历，该偏离度属于正常范围。表明该生在课堂学习与课外实践之间取得了较好的平衡。\n\n");

        // 3. 趋势预测
        report.append("### 3. 趋势预测：发展潜力评估\n");
        report.append("* **职业潜力：** 该生具备成为\"复合型\"人才的潜力，其多元化的经历为其未来发展奠定了良好基础。\n");
        report.append("* **优势领域：** 在实践应用、创新思维和团队协作方面表现突出。\n\n");

        // 4. 建议
        report.append("### 4. 建议：个性化发展指导\n");
        report.append(graphEditDistance > 10 ?
                "建议在保持现有优势的同时，尝试担任技术类项目的负责人，将\"组织领导力\"与\"技术创新\"结合。\n\n" :
                "建议继续深耕专业领域，通过参与高水平学科竞赛将知识点串联起来。\n\n");

        // 总结
        report.append("**总结：** 该生是一位具备综合素质的人才苗子。建议通过参与高水平竞赛和科研项目，将现有的实践经验转化为专业核心竞争力。");

        // 保存报告记录
        ProfileRecord record = new ProfileRecord();
        record.setStudentNo(studentNo);
        record.setReportContent(report.toString());
        record.setProfileType(profileType);
        record.setKnowledgeEntropy(knowledgeEntropy);
        record.setGraphEditDistance(graphEditDistance);
        record.setCreateTime(LocalDateTime.now());
        profileRecordRepository.save(record);

        return report.toString();
    }

    private String getTypeDescription(String profileType) {
        return switch (profileType) {
            case "学术研究型" -> "该生展现出较强的学术研究潜力和专业深度导向。";
            case "实践应用型" -> "该生展现出较强的实践能力和应用导向。";
            case "创新创业型" -> "该生展现出较强的创新意识和创业潜质。";
            default -> "该生展现出多元化的学习和发展倾向。";
        };
    }
}
