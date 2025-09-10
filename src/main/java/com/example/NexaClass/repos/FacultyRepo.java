package com.example.NexaClass.repos;

import com.example.NexaClass.entities.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacultyRepo extends JpaRepository<Faculty,Long> {
    Optional<Faculty>findByUserName(String userName);
    Optional<Faculty>findByEmail(String Email);
}
