package com.example.NexaClass.repos;

import com.example.NexaClass.entities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepo extends JpaRepository<Activity,Integer> {
    List<Activity>findByFacultyId(Integer id);
    List<Activity>findBySessionId(Integer id);
    List<Activity>findByActivityId(Integer id);
}
