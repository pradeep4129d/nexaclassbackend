package com.example.NexaClass.repos;

import com.example.NexaClass.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepo extends JpaRepository<Task,Integer> {
    List<Task> findByFacultyId(Integer id);
}
