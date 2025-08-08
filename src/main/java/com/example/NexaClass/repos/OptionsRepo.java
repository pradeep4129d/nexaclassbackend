package com.example.NexaClass.repos;

import com.example.NexaClass.entities.Options;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionsRepo extends JpaRepository<Options,Integer> {
    List<Options>findByQuestionId(Integer id);
}
