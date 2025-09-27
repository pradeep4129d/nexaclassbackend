package com.example.NexaClass.controllers;

import com.example.NexaClass.DTO.AuthRequest;
import com.example.NexaClass.entities.ClassRoom;
import com.example.NexaClass.entities.Student;
import com.example.NexaClass.repos.ClassRoomRepo;
import com.example.NexaClass.repos.StudentRepo;
import com.example.NexaClass.utilities.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentRepo studentRepo;
    @Autowired
    ClassRoomRepo classRoomRepo;
    @Autowired
    JwtUtil jwtUtil;
    @GetMapping("/classrooms")
    public ResponseEntity<?>getClassRooms(Authentication authentication){
        String username = authentication.getName();
        Optional<Student>student=studentRepo.findByEmail(username);
        if(student.isPresent()){
            Student s=student.get();
            List<ClassRoom>crs=classRoomRepo.findByBranchAndSemesterAndSection(s.getBranch(),s.getSemester(),s.getSection().toLowerCase());
            return ResponseEntity.ok(crs);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("student not found");
    }
}
