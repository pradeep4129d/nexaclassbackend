package com.example.NexaClass.controllers;

import com.example.NexaClass.DTO.AuthRequest;
import com.example.NexaClass.repos.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepo studentRepo;
    @PostMapping("/all")
    public ResponseEntity<?>getStudents(@RequestBody AuthRequest request){
        if(studentRepo.findByEmail(request.getEmail()).isPresent()){
            return ResponseEntity.ok(studentRepo.findByEmail(request.getEmail()));
        }
        return ResponseEntity.badRequest().body("user not found");
    }

}
