package com.example.NexaClass.entities;

import jakarta.persistence.*;

@Entity
@Table
public class ActivityReports {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private int facultyId;
    @Column(nullable = false)
    private int sessionId;
    @Column
    private String type;
    @Column(nullable = false, unique = true)
    private int activityId;
    @Column
    private boolean test;
    @Column
    private int studentId;
    @Column
    private boolean passed;
    @Column
    private int marksScored;
    @Column(columnDefinition = "TEXT")
    private String taskAnswer;
    @Column(columnDefinition = "TEXT")
    private String quizAnswers;
    public ActivityReports(){}
    public ActivityReports(int id, int facultyId, int sessionId, String type, int activityId, boolean test, int studentId, boolean passed, int marksScored, String taskAnswer, String quizAnswers) {
        this.id = id;
        this.facultyId = facultyId;
        this.sessionId = sessionId;
        this.type = type;
        this.activityId = activityId;
        this.test = test;
        this.studentId = studentId;
        this.passed = passed;
        this.marksScored = marksScored;
        this.taskAnswer = taskAnswer;
        this.quizAnswers = quizAnswers;
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
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public int getMarksScored() {
        return marksScored;
    }

    public void setMarksScored(int marksScored) {
        this.marksScored = marksScored;
    }

    public String getTaskAnswer() {
        return taskAnswer;
    }

    public void setTaskAnswer(String taskAnswer) {
        this.taskAnswer = taskAnswer;
    }

    public String getQuizAnswers() {
        return quizAnswers;
    }

    public void setQuizAnswers(String quizAnswers) {
        this.quizAnswers = quizAnswers;
    }
}
