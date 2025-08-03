package com.example.NexaClass.services;

import com.example.NexaClass.repos.FacultyRepo;
import com.example.NexaClass.repos.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private StudentRepo studentRepo;
    @Autowired private FacultyRepo facultyRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return studentRepo.findByEmail(email)
                .map(s -> new CustomUserDetails(s.getEmail(), s.getPassword(), s.getRole()))
                .or(() -> facultyRepo.findByEmail(email)
                        .map(f -> new CustomUserDetails(f.getEmail(), f.getPassword(), f.getRole())))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

