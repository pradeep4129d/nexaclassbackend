package com.example.NexaClass.repos;

import com.example.NexaClass.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepo extends JpaRepository<Task,Integer> {
    List<Task>findByFacultyId(Integer id);
}
