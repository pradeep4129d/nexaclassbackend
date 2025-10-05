package com.example.NexaClass.repos;

import com.example.NexaClass.entities.Activity;
import com.example.NexaClass.entities.ActivityReports;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityReportsRepo extends JpaRepository<ActivityReports,Integer> {
    List<ActivityReports> findByFacultyId(Integer id);
    List<ActivityReports>findBySessionId(Integer id);
    List<ActivityReports>findByStudentId(Integer id);
    Optional<ActivityReports>findByActivityId(Integer id);
    Optional<ActivityReports>findByActivityIdAndStudentId(Integer activityId,Integer studentId);
}
