package com.example.NexaClass.repos;

import com.example.NexaClass.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepo extends JpaRepository<Student,Integer> {
    Optional<Student>findByUserName(String userName);
    Optional<Student>findByEmail(String Email);
    Optional<Student>findById(Integer id);
}
