package com.example.NexaClass.repos;

import com.example.NexaClass.entities.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClassRoomRepo extends JpaRepository<ClassRoom,Integer> {
    Optional<ClassRoom> findByFacultyId(Integer id);
}
