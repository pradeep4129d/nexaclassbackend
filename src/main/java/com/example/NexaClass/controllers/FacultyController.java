package com.example.NexaClass.controllers;

import com.example.NexaClass.DTO.AuthRequest;
import com.example.NexaClass.DTO.AuthResponse;
import com.example.NexaClass.DTO.ProfileRequest;
import com.example.NexaClass.entities.*;
import com.example.NexaClass.repos.*;
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
    @Autowired
    SessionRepo sessionRepo;
    @Autowired
    ClassMemberRepo classMemberRepo;
    @Autowired
    QuizRepo quizRepo;
    @Autowired
    QuestionsRepo questionsRepo;
    @Autowired
    OptionsRepo optionsRepo;
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
    @DeleteMapping("/classroom/{id}")
    public ResponseEntity<?>deleteCR(@PathVariable int id,Authentication authentication){
        String username = authentication.getName();
        Optional<Faculty> faculty =facultyRepo.findByEmail(username);
        if(faculty.isPresent()){
            Optional<ClassRoom>cr=classRoomRepo.findById(id);
            if(cr.isPresent() && cr.get().getFacultyId()==faculty.get().getId()){
                classRoomRepo.deleteById(id);
                List<ClassRoom> crs = classRoomRepo.findByFacultyId(Integer.parseInt((Long.toString(faculty.get().getId()))));
                return ResponseEntity.ok(crs);
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("unauthorized");
    }
    @PostMapping("/session")
    public ResponseEntity<?>addSession(@RequestBody Session session){
        sessionRepo.save(session);
        List<Session>sessions=sessionRepo.findByClassRoomId(session.getClassRoomId());
        return ResponseEntity.ok(sessions);
    }
    @GetMapping("/session/{id}")
    public ResponseEntity<?>getSessions(@PathVariable int id){
        List<Session>sessions=sessionRepo.findByClassRoomId(id);
        return ResponseEntity.ok(sessions);
    }
    @DeleteMapping("/session/{id}")
    public ResponseEntity<?>deleteSession(@PathVariable int id){
        Optional<Session>s=sessionRepo.findById(id);
        int classId=-1;
        if(s.isPresent()){
            classId=s.get().getClassRoomId();
        }
        if(classId<0)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");
        sessionRepo.deleteById(id);
        List<Session>sessions=sessionRepo.findByClassRoomId(classId);
        return ResponseEntity.ok(sessions);
    }
    @GetMapping("/classmembers/{id}")
    public ResponseEntity<?>getCM(@PathVariable int id){
        List<ClassMember>cms=classMemberRepo.findByClassRoomId(id);
        return ResponseEntity.ok(cms);
    }
    @PostMapping("/quiz")
    public ResponseEntity<?>createQuiz(@RequestBody Quiz quiz,Authentication authentication){
        quizRepo.save(quiz);
        String username = authentication.getName();
        Optional<Faculty> faculty =facultyRepo.findByEmail(username);
        long id=-1;
        if(faculty.isPresent()) {
            id=faculty.get().getId();
        }
        List<Quiz>quizzes=quizRepo.findByFacultyId(Integer.parseInt((Long.toString(id))));
        return ResponseEntity.ok(quizzes);
    }
    @GetMapping("/quiz")
    public ResponseEntity<?>getQuizzed(Authentication authentication){
        String username = authentication.getName();
        Optional<Faculty> faculty =facultyRepo.findByEmail(username);
        long id=-1;
        if(faculty.isPresent()) {
            id=faculty.get().getId();
        }
        if(id==-1)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("unAuthorized");
        List<Quiz>quizzes=quizRepo.findByFacultyId(Integer.parseInt((Long.toString(id))));
        return ResponseEntity.ok(quizzes);
    }
    @DeleteMapping("/quiz/{id}")
    public ResponseEntity<?>deleteQuiz(@PathVariable int id,Authentication authentication){
        String username = authentication.getName();
        Optional<Faculty> faculty =facultyRepo.findByEmail(username);
        if(!faculty.isPresent()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("unAuthorized");
        }
        quizRepo.deleteById(id);
        List<Questions>questions=questionsRepo.findByQuizId(id);
        questionsRepo.deleteByQuizId(id);
        for(Questions q:questions){
            optionsRepo.deleteByQuestionId(q.getId());
        }
        List<Quiz>quizzes=quizRepo.findByFacultyId(Integer.parseInt((Long.toString(faculty.get().getId()))));
        return ResponseEntity.ok(quizzes);
    }
}
