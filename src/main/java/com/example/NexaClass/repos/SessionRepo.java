package com.example.NexaClass.repos;

import com.example.NexaClass.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepo extends JpaRepository<Session,Integer> {
    List<Session> findByClassRoomId(Integer id);
}
