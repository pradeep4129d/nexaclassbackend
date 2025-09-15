package com.example.NexaClass.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Activity")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private int facultyId;
    @Column(nullable = false)
    private int sessionId;
    @Column
    private String type;
    @Column(nullable = false)
    private int activityId;
    @Column
    private boolean isTest;
    @Column
    private boolean includeEditor;
    @Column
    private LocalDateTime start;
    @Column
    private LocalDateTime end;

    public Activity() {
    }

    public Activity(int id, int facultyId, int sessionId, String type, int activityId, boolean isTest, boolean includeEditor, LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.facultyId = facultyId;
        this.sessionId = sessionId;
        this.type = type;
        this.activityId = activityId;
        this.isTest = isTest;
        this.includeEditor = includeEditor;
        this.start = start;
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
    }

    public boolean isIncludeEditor() {
        return includeEditor;
    }

    public void setIncludeEditor(boolean includeEditor) {
        this.includeEditor = includeEditor;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
