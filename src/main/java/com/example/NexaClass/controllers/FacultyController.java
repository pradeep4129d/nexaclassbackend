package com.example.NexaClass.controllers;

import com.example.NexaClass.DTO.AuthRequest;
import com.example.NexaClass.DTO.AuthResponse;
import com.example.NexaClass.DTO.ProfileRequest;
import com.example.NexaClass.entities.ClassRoom;
import com.example.NexaClass.entities.Faculty;
import com.example.NexaClass.entities.Student;
import com.example.NexaClass.repos.ClassRoomRepo;
import com.example.NexaClass.repos.FacultyRepo;
import com.example.NexaClass.utilities.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    @Autowired
    FacultyRepo facultyRepo;
    @Autowired
    ClassRoomRepo classRoomRepo;
    @Autowired
    JwtUtil jwtUtil;
    @GetMapping("/classroom")
    public ResponseEntity<?>getCR(Authentication authentication) {
        String username = authentication.getName();
        Optional<Faculty> faculty =facultyRepo.findByEmail(username);
        if(faculty.isPresent()){
            List<ClassRoom> crs = classRoomRepo.findByFacultyId(Integer.parseInt((Long.toString(faculty.get().getId()))));
            return ResponseEntity.ok(crs);
        }
        return ResponseEntity.status(402).body("unauthorized");
    }
    @PostMapping("/classroom")
    public ResponseEntity<?>createCR(@RequestBody ClassRoom classRoom) {
        classRoomRepo.save(classRoom);
        List<ClassRoom> crs = classRoomRepo.findByFacultyId(classRoom.getFacultyId());
        return ResponseEntity.ok(crs);
    }
}
