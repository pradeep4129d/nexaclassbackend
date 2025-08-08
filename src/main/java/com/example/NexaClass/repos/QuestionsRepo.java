package com.example.NexaClass.repos;

import com.example.NexaClass.entities.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionsRepo extends JpaRepository<Questions,Integer> {
    List<Questions>findByQuizId(Integer id);
    List<Questions>findByTaskId(Integer id);
}
