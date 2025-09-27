package com.example.NexaClass.repos;

import com.example.NexaClass.entities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityRepo extends JpaRepository<Activity,Integer> {
    List<Activity> findByFacultyId(Integer id);
    List<Activity>findBySessionId(Integer id);
    Optional<Activity>findByActivityId(Integer id);
}
