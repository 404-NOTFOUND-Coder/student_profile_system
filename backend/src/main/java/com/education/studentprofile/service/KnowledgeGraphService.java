package com.education.studentprofile.service;

import com.education.studentprofile.entity.neo4j.*;
import com.education.studentprofile.repository.neo4j.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class KnowledgeGraphService {

    private final StudentGraphRepository studentGraphRepository;
    private final ActivityGraphRepository activityGraphRepository;
    private final CollegeGraphRepository collegeGraphRepository;
    private final MajorGraphRepository majorGraphRepository;

    public Map<String, Object> getGlobalGraph() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> nodes = new ArrayList<>();
        List<Map<String, Object>> links = new ArrayList<>();

        // 获取所有学生节点
        Iterable<StudentNode> students = studentGraphRepository.findAll();
        Set<String> studentNos = new HashSet<>();
        students.forEach(s -> {
            Map<String, Object> node = new HashMap<>();
            node.put("id", "student_" + s.getStudentNo());
            node.put("name", s.getName());
            node.put("type", "student");
            node.put("studentNo", s.getStudentNo());
            node.put("profileType", s.getProfileType());
            nodes.add(node);
            studentNos.add(s.getStudentNo());
        });

        // 获取所有活动节点
        Iterable<ActivityNode> activities = activityGraphRepository.findAll();
        activities.forEach(a -> {
            Map<String, Object> node = new HashMap<>();
            node.put("id", "activity_" + a.getName().hashCode());
            node.put("name", a.getName());
            node.put("type", "activity");
            node.put("level", a.getLevel());
            nodes.add(node);
        });

        // 获取所有学院节点
        Iterable<CollegeNode> colleges = collegeGraphRepository.findAll();
        colleges.forEach(c -> {
            Map<String, Object> node = new HashMap<>();
            node.put("id", "college_" + c.getName().hashCode());
            node.put("name", c.getName());
            node.put("type", "college");
            nodes.add(node);
        });

        result.put("nodes", nodes);
        result.put("links", links);
        return result;
    }

    public Map<String, Object> getStudentGraph(String studentNo) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> nodes = new ArrayList<>();
        List<Map<String, Object>> links = new ArrayList<>();

        // 获取学生信息
        Optional<StudentNode> studentOpt = studentGraphRepository.findByStudentNo(studentNo);
        if (studentOpt.isEmpty()) {
            return result;
        }

        StudentNode student = studentOpt.get();
        Map<String, Object> studentNode = new HashMap<>();
        studentNode.put("id", "student_" + student.getStudentNo());
        studentNode.put("name", student.getName());
        studentNode.put("type", "student");
        studentNode.put("studentNo", student.getStudentNo());
        studentNode.put("profileType", student.getProfileType());
        nodes.add(studentNode);

        result.put("nodes", nodes);
        result.put("links", links);
        return result;
    }

    public void saveStudentNode(StudentNode node) {
        studentGraphRepository.save(node);
    }

    public void saveActivityNode(ActivityNode node) {
        activityGraphRepository.save(node);
    }

    public void saveCollegeNode(CollegeNode node) {
        collegeGraphRepository.save(node);
    }
}
