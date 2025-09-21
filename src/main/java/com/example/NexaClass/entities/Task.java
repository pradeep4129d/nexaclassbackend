package com.example.NexaClass.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private int facultyId;
    @Column
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column
    private int marks;
    @Column
    private boolean eval;
    public Task(){}
    public Task(int id, int facultyId, String title, String description, int marks,boolean eval) {
        this.id = id;
        this.facultyId = facultyId;
        this.title = title;
        this.description = description;
        this.marks = marks;
        this.eval=eval;
    }

    public boolean isEval() {
        return eval;
    }

    public void setEval(boolean eval) {
        this.eval = eval;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }
}
