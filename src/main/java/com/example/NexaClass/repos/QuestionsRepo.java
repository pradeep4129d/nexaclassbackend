package com.example.NexaClass.repos;

import com.example.NexaClass.entities.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionsRepo extends JpaRepository<Questions,Integer> {
    List<Questions> findByQuizId(Integer id);
    List<Questions>findByTaskId(Integer id);
    void deleteByQuizId(Integer id);
    void deleteByTaskId(Integer id);
    Questions findTopByOrderByIdDesc();
}
