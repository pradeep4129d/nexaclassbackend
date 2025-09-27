package com.example.NexaClass.controllers;

import com.example.NexaClass.DTO.QuestionDTO;
import com.example.NexaClass.entities.*;
import com.example.NexaClass.repos.*;
import com.example.NexaClass.utilities.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    @Autowired
    TaskRepo taskRepo;
    @Autowired
    ActivityRepo activityRepo;
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
    @GetMapping("/quiz/{id}")
    public ResponseEntity<?>getQuiz(@PathVariable int id){
        Optional<Quiz>q=quizRepo.findById(id);
        Optional<Task>t=taskRepo.findById(id);
        if(q.isPresent()){
            return ResponseEntity.ok(q.get());
        }else if(t.isPresent())
            return ResponseEntity.ok(t.get());
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("quiz not found");
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
    @PostMapping("/question")
    public ResponseEntity<?>addQuestion(@RequestBody QuestionDTO questionDTO){
        Questions question=new Questions(questionDTO.getQuizId(), questionDTO.getTaskId(), questionDTO.getDescription(), questionDTO.getAnswer());
        questionsRepo.save(question);
        if(questionDTO.getQuizId()<=-1){
            List<Questions>q=questionsRepo.findByTaskId(questionDTO.getTaskId());
            return ResponseEntity.ok(q);
        }
        Questions recentQuestion=questionsRepo.findTopByOrderByIdDesc();
        for(Options option: questionDTO.getOptions()){
            option.setQuestionId(recentQuestion.getId());
            optionsRepo.save(option);
        }
        List<QuestionDTO>res=new ArrayList<>();
        List<Questions>AllQuestions=questionsRepo.findByQuizId(questionDTO.getQuizId());
        for(Questions questions:AllQuestions){
            List<Options>options=optionsRepo.findByQuestionId(questions.getId());
            res.add(new QuestionDTO(questions.getQuizId(), questions.getTaskId(), questions.getDescription(), questions.getAnswer(), options));
        }
        return ResponseEntity.ok(res);
    }
    @GetMapping("/question/{id}")
    public ResponseEntity<?>getQuestion(@PathVariable int id){
        System.out.println(id);
        List<QuestionDTO>res=new ArrayList<>();
        List<Questions>AllQ=questionsRepo.findByTaskId(id);
        if(!AllQ.isEmpty())
            return ResponseEntity.ok(AllQ);
        List<Questions>AllQuestions=questionsRepo.findByQuizId(id);
        for(Questions questions:AllQuestions){
            List<Options>options=optionsRepo.findByQuestionId(questions.getId());
            res.add(new QuestionDTO(questions.getId(), questions.getQuizId(), questions.getTaskId(), questions.getDescription(), questions.getAnswer(), options));
        }
        return ResponseEntity.ok(res);
    }
    @DeleteMapping("/question/{id}")
    @Transactional
    public ResponseEntity<?>deleteQuestion(@PathVariable int id){
        int quizId=questionsRepo.findById(id).get().getQuizId();
        int taskId=questionsRepo.findById(id).get().getTaskId();
        questionsRepo.deleteById(id);
        optionsRepo.deleteByQuestionId(id);
        if (quizId>=0){
            List<QuestionDTO>res=new ArrayList<>();
            List<Questions>AllQuestions=questionsRepo.findByQuizId(quizId);
            for(Questions questions:AllQuestions){
                List<Options>options=optionsRepo.findByQuestionId(questions.getId());
                res.add(new QuestionDTO(questions.getId(),questions.getQuizId(), questions.getTaskId(), questions.getDescription(), questions.getAnswer(), options));
            }
            return ResponseEntity.ok(res);
        }
        List<Questions>AllQuestions=questionsRepo.findByTaskId(taskId);
        return ResponseEntity.ok(AllQuestions);
    }
    @PutMapping("/quiz")
    public ResponseEntity<?>updateQuiz(@RequestBody Quiz quiz){
        Optional<Quiz>q=quizRepo.findById(quiz.getId());
        if(q.isPresent()){
            Quiz updatedQuiz=q.get();
            updatedQuiz.setFacultyId(quiz.getFacultyId());
            updatedQuiz.setDescription(quiz.getDescription());
            updatedQuiz.setTitle(quiz.getTitle());
            updatedQuiz.setMarksForCorrect(quiz.getMarksForCorrect());
            updatedQuiz.setNegativeMarks(quiz.getNegativeMarks());
            updatedQuiz.setPassingMarks(quiz.getPassingMarks());
            quizRepo.save(updatedQuiz);
            return ResponseEntity.status(HttpStatus.OK).body("updated successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("quiz not found");
    }
    @PutMapping("/task")
    public ResponseEntity<?>updateTask(@RequestBody Task task){
        Optional<Task>t=taskRepo.findById(task.getId());
        if(t.isPresent()){
            Task updatedTask=t.get();
            updatedTask.setFacultyId(task.getFacultyId());
            updatedTask.setDescription(task.getDescription());
            updatedTask.setTitle(task.getTitle());
            updatedTask.setEval(task.isEval());
            updatedTask.setMarks(task.getMarks());
            taskRepo.save(updatedTask);
            return ResponseEntity.status(HttpStatus.OK).body("updated successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("task not found");
    }
    @PostMapping("/task")
    public ResponseEntity<?>createTask(@RequestBody Task task){
        int facultyId= task.getFacultyId();
        taskRepo.save(task);
        List<Task>tasks=taskRepo.findByFacultyId(facultyId);
        return ResponseEntity.ok(tasks);
    }
    @GetMapping("/task")
    public ResponseEntity<?>getTasks(Authentication authentication){
        String username = authentication.getName();
        Optional<Faculty> faculty =facultyRepo.findByEmail(username);
        if(faculty.isPresent()){
            List<Task> tasks = taskRepo.findByFacultyId(Integer.parseInt((Long.toString(faculty.get().getId()))));
            return ResponseEntity.ok(tasks);
        }
        return ResponseEntity.status(402).body("unauthorized");
    }
    @DeleteMapping("/task/{id}")
    public ResponseEntity<?>deleteTask(@PathVariable int id,Authentication authentication){
        String username = authentication.getName();
        Optional<Faculty> faculty =facultyRepo.findByEmail(username);
        if(!faculty.isPresent()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("unAuthorized");
        }
        taskRepo.deleteById(id);
        questionsRepo.deleteByTaskId(id);
        List<Task>tasks=taskRepo.findByFacultyId(Integer.parseInt((Long.toString(faculty.get().getId()))));
        return ResponseEntity.ok(tasks);
    }
    @GetMapping("/activity/{id}")
    public ResponseEntity<?>getActivities(@PathVariable int id){
        return ResponseEntity.ok(activityRepo.findBySessionId(id));
    }
    @PostMapping("/activity")
    public ResponseEntity<?>createActivity(@RequestBody Activity activity){
        activityRepo.save(activity);
        List<Activity>activities=activityRepo.findBySessionId(activity.getSessionId());
        return ResponseEntity.ok(activities);
    }
    @DeleteMapping("/activity/{id}")
    public ResponseEntity<?>deleteActivity(@PathVariable int id){
        int sessionId=activityRepo.findById(id).get().getSessionId();
        activityRepo.deleteById(id);
        return ResponseEntity.ok(activityRepo.findBySessionId(sessionId));
    }

}
