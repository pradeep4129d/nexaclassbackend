package com.example.NexaClass.DTO;

public class ActivityReportsDTO {
    private String sessionTitle;
    private String studentUserName;
    private String type;
    private boolean test;
    private boolean passed;
    private int marksScored;
    private String taskAnswers;
    private String quizAnswers;
    public ActivityReportsDTO(){};

    public ActivityReportsDTO(String sessionTitle, String studentUserName, String type, boolean test, boolean passed, int marksScored, String taskAnswers, String quizAnswers) {
        this.sessionTitle = sessionTitle;
        this.studentUserName = studentUserName;
        this.type = type;
        this.test = test;
        this.passed = passed;
        this.marksScored = marksScored;
        this.taskAnswers = taskAnswers;
        this.quizAnswers = quizAnswers;
    }

    public String getSessionTitle() {
        return sessionTitle;
    }

    public void setSessionTitle(String sessionTitle) {
        this.sessionTitle = sessionTitle;
    }

    public String getStudentUserName() {
        return studentUserName;
    }

    public void setStudentUserName(String studentUserName) {
        this.studentUserName = studentUserName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
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

    public String getTaskAnswers() {
        return taskAnswers;
    }

    public void setTaskAnswers(String taskAnswers) {
        this.taskAnswers = taskAnswers;
    }

    public String getQuizAnswers() {
        return quizAnswers;
    }

    public void setQuizAnswers(String quizAnswers) {
        this.quizAnswers = quizAnswers;
    }
}
