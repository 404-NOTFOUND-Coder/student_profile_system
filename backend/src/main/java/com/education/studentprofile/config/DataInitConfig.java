package com.education.studentprofile.config;

import com.education.studentprofile.entity.mysql.Activity;
import com.education.studentprofile.entity.mysql.Student;
import com.education.studentprofile.entity.mysql.Warning;
import com.education.studentprofile.entity.neo4j.ActivityNode;
import com.education.studentprofile.entity.neo4j.CollegeNode;
import com.education.studentprofile.entity.neo4j.StudentNode;
import com.education.studentprofile.repository.mysql.ActivityRepository;
import com.education.studentprofile.repository.mysql.StudentRepository;
import com.education.studentprofile.repository.mysql.WarningRepository;
import com.education.studentprofile.repository.neo4j.ActivityGraphRepository;
import com.education.studentprofile.repository.neo4j.CollegeGraphRepository;
import com.education.studentprofile.repository.neo4j.StudentGraphRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitConfig implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final ActivityRepository activityRepository;
    private final WarningRepository warningRepository;
    private final StudentGraphRepository studentGraphRepository;
    private final ActivityGraphRepository activityGraphRepository;
    private final CollegeGraphRepository collegeGraphRepository;

    @Override
    public void run(String... args) {
        // Only initialize if database is empty
        if (studentRepository.count() == 0) {
            log.info("Initializing sample data...");
            initStudents();
            initActivities();
            initWarnings();
            initGraphData();
            log.info("Sample data initialization completed!");
        }
    }

    private void initStudents() {
        Student[] students = {
            createStudent("2020010906005", "张伟", 1, "计算机科学与工程学院（网络空间安全学院）", "计算机科学与技术", "1班", 2020, 1, 88.0, 8.5, 2.3, 92.0, "学术研究型", "A", "勤奋好学,逻辑思维强,创新意识高", "编程达人,算法高手,科研新星", "竞赛选手,项目骨干,技术分享者", "加强实践经验,提升团队协作,关注前沿技术", "RISING", "学业表现稳步提升"),
            createStudent("2020010906012", "李娜", 2, "计算机科学与工程学院（网络空间安全学院）", "软件工程", "1班", 2020, 1, 75.0, 7.2, 3.5, 68.0, "实践应用型", "B+", "动手能力强,善于实践,团队协作好", "实践达人,项目经验丰富", "学生干部,志愿者标兵,社团活跃分子", "加强理论学习,提升学术研究能力", "STABLE", "学业表现稳定"),
            createStudent("2020010906013", "王丽", 2, "信息与软件工程学院（示范性软件学院）", "软件工程", "1班", 2020, 1, 78.0, 7.8, 2.8, 85.0, "复合探索型", "B+", "兴趣广泛,创意无限,审美能力强", "设计达人,创意先锋", "社会实践,艺术活动,新媒体运营", "深化专业技能,注重成果积累", "RISING", "学业表现稳中有升"),
            createStudent("2020010906020", "刘洋", 1, "电子科学与工程学院", "电子科学与技术", "1班", 2020, 1, 82.0, 7.5, 2.5, 78.0, "学术研究型", "A-", "认真踏实,科研潜力大,数理基础扎实", "学术新星,竞赛选手", "科研项目,学科竞赛", "拓宽国际视野,加强交流合作", "RISING", "学业表现优秀且持续进步"),
            createStudent("2020010906021", "陈静", 2, "机械与电气工程学院", "机械设计制造及其自动化", "1班", 2020, 2, 65.0, 6.5, 4.2, 55.0, "待分析", "C", "需要关注,学业困难", "学业困难", "", "寻求帮助,制定学习计划", "DECLINING", "学业表现下滑")
        };

        for (Student student : students) {
            studentRepository.save(student);
        }
    }

    private Student createStudent(String studentNo, String name, int gender, String college, String major,
            String className, int enrollmentYear, int studentStatus, double profileScore,
            double knowledgeEntropy, double graphEditDistance, double activityScore,
            String profileType, String abilityLevel, String personality,
            String academicTags, String practiceTags, String suggestionTags,
            String trendStatus, String trendDesc) {
        Student s = new Student();
        s.setStudentNo(studentNo);
        s.setName(name);
        s.setGender(gender);
        s.setCollege(college);
        s.setMajor(major);
        s.setClassName(className);
        s.setEnrollmentYear(enrollmentYear);
        s.setStudentStatus(studentStatus);
        s.setProfileScore(profileScore);
        s.setKnowledgeEntropy(knowledgeEntropy);
        s.setGraphEditDistance(graphEditDistance);
        s.setActivityScore(activityScore);
        s.setProfileType(profileType);
        s.setAbilityLevel(abilityLevel);
        s.setPersonality(personality);
        s.setAcademicTags(academicTags);
        s.setPracticeTags(practiceTags);
        s.setSuggestionTags(suggestionTags);
        s.setAcademicTrendStatus(trendStatus);
        s.setAcademicTrendDesc(trendDesc);
        return s;
    }

    private void initActivities() {
        Activity[] activities = {
            createActivity("ACM-ICPC国际大学生程序设计竞赛", "科技创新", "国家级", "2023-05", "亚洲区域赛银奖", "2020010906005,2020010906012", "国家级_二等奖"),
            createActivity("全国大学生程序设计竞赛", "科技创新", "国家级", "2022-10", "获得一等奖", "2020010906005", "国家级_一等奖"),
            createActivity("人工智能创新实验", "学术研究", "校级", "2023-03", "优秀项目", "2020010906005", null),
            createActivity("暑期三下乡社会实践", "志愿服务", "院级", "2022-07", "支教活动", "2020010906005,2020010906013", null),
            createActivity("全国大学生数学建模竞赛", "学科竞赛", "省部级", "2023-09", "获得二等奖", "2020010906012", "省部级_二等奖"),
            createActivity("区块链技术研究", "学术研究", "校级", "2023-06", "发表论文", "2020010906012", null),
            createActivity("全国大学生广告艺术大赛", "文化艺术", "国家级", "2023-08", "优秀作品奖", "2020010910008", "国家级_优秀奖"),
            createActivity("互联网公司实习", "社会实践", "校级", "2023-07", "暑期实习", "2020010910008", null),
            createActivity("全国大学生电子设计竞赛", "科技创新", "国家级", "2023-04", "获得一等奖", "2020010913014", "国家级_一等奖"),
            createActivity("5G通信技术研究", "学术研究", "校级", "2023-01", "科研项目", "2020010913014", null),
            createActivity("大学生创新创业大赛", "科技创新", "省部级", "2023-11", "创业项目", "2020010906005,2020010906012,2020010910008", "省部级_铜奖"),
            createActivity("程序设计校赛", "科技创新", "校级", "2023-06", "个人赛", "2020010906005", "校级_一等奖"),
            createActivity("数学竞赛", "学科竞赛", "校级", "2023-10", "校级选拔赛", "2020010906012,2020010913014", "校级_一等奖"),
            createActivity("校园篮球赛", "体育活动", "院级", "2023-04", "团体赛", "2020010914004,2020010915001", "院级_一等奖"),
            createActivity("暑期支教活动", "志愿服务", "校级", "2022-08", "爱心支教", "2020010906005,2020010906013", null),
            createActivity("网络安全竞赛", "科技创新", "国家级", "2024-03", "获得三等奖", "2020010906005", "国家级_三等奖"),
            createActivity("英语演讲比赛", "学科竞赛", "校级", "2023-11", "二等奖", "2020010906012", "校级_二等奖"),
            createActivity("市场营销大赛", "社会实践", "省部级", "2024-01", "获得三等奖", "2020010910008,2020010910014", "省部级_三等奖"),
            createActivity("建筑设计竞赛", "学术研究", "国家级", "2023-09", "获得优秀奖", "2020010912003", "国家级_优秀奖")
        };

        for (Activity activity : activities) {
            activityRepository.save(activity);
        }
    }

    private Activity createActivity(String name, String type, String level, String time, String desc, String participants, String award) {
        Activity a = new Activity();
        a.setName(name);
        a.setType(type);
        a.setLevel(level);
        a.setTime(time);
        a.setDescription(desc);
        a.setParticipants(participants);
        a.setAward(award);
        return a;
    }

    private void initWarnings() {
        Warning[] warnings = {
            createWarning("2020010906005", "计算机科学与工程学院（网络空间安全学院）", "计算机科学与技术", "成绩预警", 1, "一般", "数据结构与算法成绩偏低", "保持当前学习状态"),
            createWarning("2020010906012", "计算机科学与工程学院（网络空间安全学院）", "软件工程", "学分预警", 2, "较重", "已获学分不足毕业要求", "加强学分修读"),
            createWarning("2020010907002", "格拉斯哥学院", "电子信息类（中外合作办学）（通信工程）", "学业困难", 1, "一般", "部分课程学习困难", "寻求辅导帮助"),
            createWarning("2020010906021", "机械与电气工程学院", "机械设计制造及其自动化", "学业困难", 3, "严重", "多门课程不及格", "需要重点关注"),
            createWarning("2020010910008", "经济与管理学院", "工商管理", "成绩预警", 1, "一般", "管理学原理成绩偏低", "加强课程学习")
        };

        for (Warning warning : warnings) {
            warningRepository.save(warning);
        }
    }

    private Warning createWarning(String studentNo, String college, String major, String warningType, int level, String levelName, String message, String suggestAction) {
        Warning w = new Warning();
        w.setStudentNo(studentNo);
        w.setCollege(college);
        w.setMajor(major);
        w.setWarningType(warningType);
        w.setLevel(level);
        w.setLevelName(levelName);
        w.setWarningMessage(message);
        w.setSuggestAction(suggestAction);
        return w;
    }

    private void initGraphData() {
        // Initialize Neo4j graph data
        for (Student student : studentRepository.findAll()) {
            StudentNode sNode = new StudentNode();
            sNode.setStudentNo(student.getStudentNo());
            sNode.setName(student.getName());
            sNode.setCollege(student.getCollege());
            sNode.setMajor(student.getMajor());
            sNode.setProfileType(student.getProfileType());
            studentGraphRepository.save(sNode);
        }

        // Save colleges
        String[] colleges = {
            "计算机科学与工程学院（网络空间安全学院）",
            "信息与软件工程学院（示范性软件学院）",
            "电子科学与工程学院",
            "机械与电气工程学院",
            "经济与管理学院",
            "格拉斯哥学院",
            "数学科学学院",
            "光电科学与工程学院",
            "生命科学与技术学院"
        };

        for (String collegeName : colleges) {
            CollegeNode node = new CollegeNode();
            node.setName(collegeName);
            collegeGraphRepository.save(node);
        }
    }
}
