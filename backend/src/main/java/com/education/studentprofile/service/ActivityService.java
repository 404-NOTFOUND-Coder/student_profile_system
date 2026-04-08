package com.education.studentprofile.service;

import com.education.studentprofile.entity.mysql.Activity;
import com.education.studentprofile.repository.mysql.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }

    public Optional<Activity> getActivityById(Long id) {
        return activityRepository.findById(id);
    }

    public List<Activity> getActivitiesByType(String type) {
        return activityRepository.findByType(type);
    }

    public List<Activity> getActivitiesByLevel(String level) {
        return activityRepository.findByLevel(level);
    }

    public List<Activity> getActivitiesByStudentNo(String studentNo) {
        return activityRepository.findByParticipant(studentNo);
    }

    public List<Activity> searchActivities(String keyword) {
        return activityRepository.searchByName(keyword);
    }

    public Activity saveActivity(Activity activity) {
        activity.setCreateTime(LocalDateTime.now());
        return activityRepository.save(activity);
    }

    public Activity updateActivity(Activity activity) {
        return activityRepository.save(activity);
    }

    public void deleteActivity(Long id) {
        activityRepository.deleteById(id);
    }
}
