package com.education.studentprofile.controller;

import com.education.studentprofile.service.KnowledgeGraphService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/knowledge-graph")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class KnowledgeGraphController {

    private final KnowledgeGraphService knowledgeGraphService;

    @GetMapping("/global")
    public ResponseEntity<Map<String, Object>> getGlobalGraph() {
        Map<String, Object> graph = knowledgeGraphService.getGlobalGraph();
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", graph);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/student/{studentNo}")
    public ResponseEntity<Map<String, Object>> getStudentGraph(@PathVariable String studentNo) {
        Map<String, Object> graph = knowledgeGraphService.getStudentGraph(studentNo);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", graph);
        return ResponseEntity.ok(result);
    }
}
