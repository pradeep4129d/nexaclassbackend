package com.example.NexaClass.repos;

import com.example.NexaClass.entities.Options;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OptionsRepo extends JpaRepository<Options,Integer> {
    List<Options> findByQuestionId(Integer id);
    void deleteByQuestionId(Integer id);
}
