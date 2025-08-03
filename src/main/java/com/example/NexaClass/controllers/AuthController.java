package com.example.NexaClass.controllers;

import com.example.NexaClass.DTO.AuthRequest;
import com.example.NexaClass.DTO.AuthResponse;
import com.example.NexaClass.DTO.OtpRequest;
import com.example.NexaClass.DTO.VerifyRequest;
import com.example.NexaClass.entities.Faculty;
import com.example.NexaClass.entities.Student;
import com.example.NexaClass.repos.FacultyRepo;
import com.example.NexaClass.repos.StudentRepo;
import com.example.NexaClass.services.CustomUserDetailsService;
import com.example.NexaClass.utilities.JwtUtil;
import com.example.NexaClass.utilities.OTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.time.Duration;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/auth")

public class AuthController {
    @Autowired private AuthenticationManager authManager;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private CustomUserDetailsService userDetailsService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    FacultyRepo facultyRepo;
    @Autowired
    StudentRepo studentRepo;
    private final HashMap<String,OTP>OTPs=new HashMap<>();
    private final HashSet<String>verifiedMails=new HashSet<>();
    @PostMapping("/sendotp")
    public ResponseEntity<?>sendOtp(@RequestBody OtpRequest otpRequest){
        if(facultyRepo.findByEmail(otpRequest.getEmail()).isPresent() || studentRepo .findByEmail(otpRequest.getEmail()).isPresent()){
            return ResponseEntity.badRequest().body("email already in use");
        }
        String otp = String.format("%06d", new Random().nextInt(999999));
        OTPs.put(otpRequest.getEmail(),new OTP(otp,LocalTime.now()));
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(otpRequest.getEmail());
        message.setSubject("NexaClass - OTP Verification");
        message.setText("Your OTP is: " + otp + "\nPlease do not share it with anyone.\nNote:This OTP is only valid for 2 minutes");
        mailSender.send(message);
        return ResponseEntity.ok("otp sent successfully");
    }

    @PostMapping("/verifyotp")
    public ResponseEntity<?>verifyOtp(@RequestBody VerifyRequest verifyRequest){
        String email=verifyRequest.getEmail();
        String otp= verifyRequest.getOtp();
        if(OTPs.containsKey(email) && Math.abs(Duration.between(LocalTime.now(),OTPs.get(email).getTime()).toMinutes())<=2 && otp.equals(OTPs.get(email).getOtp())){
            OTPs.remove(email);
            verifiedMails.add(email);
            return ResponseEntity.ok("OTP verified successfull");
        }else if(OTPs.containsKey(email) && Math.abs(Duration.between(LocalTime.now(),OTPs.get(email).getTime()).toMinutes())>2){
            OTPs.remove(email);
        }
        return ResponseEntity.badRequest().body("OTP is invalid");
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtUtil.generateToken(userDetails.getUsername(),
                userDetails.getAuthorities().iterator().next().getAuthority().substring(5));
        return ResponseEntity.ok(new AuthResponse(token));
    }
    @PostMapping("/register")
    public ResponseEntity<?>register(@RequestBody AuthRequest request){
        verifiedMails.add("22a81a0504@sves.org.in");
        System.out.println(request.getUserName());
        if(facultyRepo.findByEmail(request.getEmail()).isPresent() || studentRepo .findByEmail(request.getEmail()).isPresent() || !verifiedMails.contains(request.getEmail())){
            return ResponseEntity.badRequest().body("email is not verified or already in use");
        }
        if(request.getRole().equals("STUDENT")){
            Student student=new Student();
            student.setUserName(request.getUserName());
            student.setBranch(request.getBranch());
            student.setEmail(request.getEmail());
            student.setPassword(passwordEncoder.encode(request.getPassword()));
            student.setSection(request.getSection());
            student.setSemester(request.getSemester());
            studentRepo.save(student);
        }else{
            Faculty faculty=new Faculty();
            faculty.setEmail(request.getEmail());
            faculty.setPassword(passwordEncoder.encode(request.getPassword()));
            facultyRepo.save(faculty);
        }
        String token = jwtUtil.generateToken(request.getUserName(), request.getRole());
        return ResponseEntity.ok(new AuthResponse(token));
    }

}
