package com.example.NexaClass.repos;

import com.example.NexaClass.entities.ClassMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClassMemberRepo extends JpaRepository<ClassMember,Integer> {
    Optional<ClassMember>findByClassRoomId(Integer id);
}
