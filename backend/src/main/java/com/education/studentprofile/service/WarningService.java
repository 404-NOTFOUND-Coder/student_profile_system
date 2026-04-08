package com.education.studentprofile.service;

import com.education.studentprofile.entity.mysql.Warning;
import com.education.studentprofile.repository.mysql.WarningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WarningService {

    private final WarningRepository warningRepository;

    public List<Warning> getAllWarnings() {
        return warningRepository.findAll();
    }

    public List<Warning> getWarningsByStudentNo(String studentNo) {
        return warningRepository.findByStudentNo(studentNo);
    }

    public List<Warning> getWarningsByCollege(String college) {
        return warningRepository.findByCollege(college);
    }

    public List<Warning> getWarningsByType(String warningType) {
        return warningRepository.findByWarningType(warningType);
    }

    public List<Warning> getWarningsByLevel(Integer level) {
        return warningRepository.findByLevel(level);
    }

    public Warning saveWarning(Warning warning) {
        warning.setCreateTime(LocalDateTime.now());
        return warningRepository.save(warning);
    }

    public void deleteWarning(Long id) {
        warningRepository.deleteById(id);
    }
}
