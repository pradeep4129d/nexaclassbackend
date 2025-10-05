package com.example.NexaClass.repos;

import com.example.NexaClass.entities.Notes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotesRepo extends JpaRepository<Notes,Integer> {
    List<Notes>findByStudentId(Long id);
}
