package com.example.NexaClass.repos;

import com.example.NexaClass.entities.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuizRepo extends JpaRepository<Quiz,Integer> {
    List<Quiz> findByFacultyId(Integer id);
}
