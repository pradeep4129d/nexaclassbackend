package com.example.NexaClass.controllers;

import com.example.NexaClass.DTO.*;
import com.example.NexaClass.entities.*;
import com.example.NexaClass.repos.*;
import com.example.NexaClass.utilities.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    ClassMemberRepo classMemberRepo;
    @Autowired
    StudentRepo studentRepo;
    @Autowired
    ClassRoomRepo classRoomRepo;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    SessionRepo sessionRepo;
    @Autowired
    ActivityRepo activityRepo;
    @Autowired
    QuizRepo quizRepo;
    @Autowired
    TaskRepo taskRepo;
    @Autowired
    QuestionsRepo questionsRepo;
    @Autowired
    OptionsRepo optionsRepo;
    @Autowired
    ActivityReportsRepo activityReportsRepo;
    @Autowired
    NotesRepo notesRepo;
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
    @PostMapping("/join")
    public ResponseEntity<?>join(@RequestBody ClassMember classMember){
        try {
            classMemberRepo.save(classMember);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("something went wrong");
        }
        return ResponseEntity.ok(classMemberRepo.findByStudentId(classMember.getStudentId()));
    }
    @GetMapping("/classmembers")
    public ResponseEntity<?>getCMs(Authentication authentication){
        String username = authentication.getName();
        Optional<Student>student=studentRepo.findByEmail(username);
        if(student.isPresent()){
            Student s=student.get();
            List<ClassMember> cms =classMemberRepo.findByStudentId(Integer.parseInt((Long.toString(s.getId()))));
            return ResponseEntity.ok(cms);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("student not found");
    }
    @DeleteMapping("/classmembers/{id}")
    @Transactional
    public ResponseEntity<?>deletecm(@PathVariable int id,Authentication authentication){
        classMemberRepo.deleteByClassRoomId(id);
        String username = authentication.getName();
        Optional<Student>student=studentRepo.findByEmail(username);
        if(student.isPresent()){
            Student s=student.get();
            List<ClassMember> cms =classMemberRepo.findByStudentId(Integer.parseInt((Long.toString(s.getId()))));
            List<ClassRoom>crs=new ArrayList<>();
            for(ClassMember cm:cms){
                crs.add(classRoomRepo.findById(cm.getClassRoomId()).get());
            }
            return ResponseEntity.ok(crs);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("student not found");
    }
    @GetMapping("/joinedclasses")
    public ResponseEntity<?>getJoinedClasses(Authentication authentication){
        String username = authentication.getName();
        Optional<Student>student=studentRepo.findByEmail(username);
        if(student.isPresent()){
            Student s=student.get();
            List<ClassMember> cms =classMemberRepo.findByStudentId(Integer.parseInt((Long.toString(s.getId()))));
            List<ClassRoom>crs=new ArrayList<>();
            for(ClassMember cm:cms){
                crs.add(classRoomRepo.findById(cm.getClassRoomId()).get());
            }
            return ResponseEntity.ok(crs);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("student not found");
    }
    @GetMapping("/session/{id}")
    public ResponseEntity<?>getSessions(@PathVariable int id){
        List<Session>sessions=sessionRepo.findByClassRoomId(id);
        return ResponseEntity.ok(sessions);
    }
    @GetMapping("/activities/{id}")
    public ResponseEntity<?>getActivities(@PathVariable int id){
        List<Activity>activities=activityRepo.findBySessionId(id);
        List<ActivityDTO>res=new ArrayList<>();
        for(Activity activity:activities){
            Quiz quiz=null;
            Task task=null;
            if(activity.getType().equals("quiz")){
                Optional<Quiz>q=quizRepo.findById(activity.getActivityId());
                quiz= q.orElse(null);
            }else{
                Optional<Task>t=taskRepo.findById(activity.getActivityId());
                task=t.orElse(null);
            }
            ActivityDTO activityDTO=new ActivityDTO(activity.getId(),activity.getFacultyId(), activity.getSessionId(),activity.getType(),activity.getActivityId(),activity.isTest(),activity.isIncludeEditor(),quiz,task);
            res.add(activityDTO);
        }
        return ResponseEntity.ok(res);
    }
    @PostMapping("/questions")
    public ResponseEntity<?>getQuestions(@RequestBody TestActivityDTO testActivityDTO){
        if(testActivityDTO.getType().equals("quiz")) {
            List<Questions> questions = questionsRepo.findByQuizId(testActivityDTO.getId());
            List<QuizQuestionsDTO> res = new ArrayList<>();
            for (Questions question : questions) {
                List<Options> options = optionsRepo.findByQuestionId(question.getId());
                QuizQuestionsDTO quizQuestionsDTO = new QuizQuestionsDTO(question, options);
                res.add(quizQuestionsDTO);
            }
            return ResponseEntity.ok(res);
        }
        List<Questions>questions=questionsRepo.findByTaskId(testActivityDTO.getId());
        return ResponseEntity.ok(questions);
    }
    @PostMapping("/submit")
    public ResponseEntity<?>submitTest(@RequestBody ActivityReports activityReport){
        activityReportsRepo.save(activityReport);
        List<ActivityReports>activityReports=activityReportsRepo.findByStudentId(activityReport.getStudentId());
        return ResponseEntity.ok(activityReports);
    }
    @GetMapping("/attempted")
    public  ResponseEntity<?>getAttemptedIds(Authentication authentication){
        String username = authentication.getName();
        Optional<Student>student=studentRepo.findByEmail(username);
        if(student.isPresent()){
            List<ActivityReports>activityReports=activityReportsRepo.findByStudentId(Integer.parseInt((Long.toString(student.get().getId()))));
            return ResponseEntity.ok(activityReports);
        }
        return ResponseEntity.status(404).body("not found");
    }
    @GetMapping("/notes")
    public ResponseEntity<?>getMyNotes(Authentication authentication){
        String username = authentication.getName();
        Optional<Student>student=studentRepo.findByEmail(username);
        if(student.isPresent()){
            return ResponseEntity.ok(notesRepo.findByStudentId(student.get().getId()));
        }
        return ResponseEntity.status(403).body("forbidden");
    }
}
