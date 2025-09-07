package com.example.NexaClass.repos;

import com.example.NexaClass.entities.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionsRepo extends JpaRepository<Questions,Integer> {
    Optional<Questions> findByQuizId(Integer id);
    Optional<Questions>findByTaskId(Integer id);
}
