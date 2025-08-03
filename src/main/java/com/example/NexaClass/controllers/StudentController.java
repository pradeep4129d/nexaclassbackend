package com.example.NexaClass.controllers;

import com.example.NexaClass.DTO.AuthRequest;
import com.example.NexaClass.entities.Student;
import com.example.NexaClass.repos.StudentRepo;
import com.example.NexaClass.utilities.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepo studentRepo;
    @Autowired
    JwtUtil jwtUtil;
    @GetMapping("/profile")
    public ResponseEntity<?>getStudents(Authentication authentication){
        Optional<Student>student =studentRepo.findByEmail(authentication.getName());
        if(student.isPresent()){
            return ResponseEntity.ok(student);
        }
        return ResponseEntity.badRequest().body("user not found");
    }

}
