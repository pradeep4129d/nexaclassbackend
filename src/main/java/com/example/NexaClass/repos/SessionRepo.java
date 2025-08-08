package com.example.NexaClass.repos;

import com.example.NexaClass.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepo extends JpaRepository<SessionRepo,Integer> {
    List<Session>findByClassRoomId(Integer id);
}
