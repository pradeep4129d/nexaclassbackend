package com.example.NexaClass.DTO;

import com.example.NexaClass.entities.Quiz;
import com.example.NexaClass.entities.Task;

public class ActivityDTO {
    private int id;
    private int facultyId;
    private int sessionId;
    private String type;
    private int activityId;
    private boolean test;
    private boolean includeEditor;
    private Quiz quiz;
    private Task task;

    public ActivityDTO(int id, int facultyId, int sessionId, String type, int activityId, boolean test, boolean includeEditor, Quiz quiz, Task task) {
        this.id = id;
        this.facultyId = facultyId;
        this.sessionId = sessionId;
        this.type = type;
        this.activityId = activityId;
        this.test = test;
        this.includeEditor = includeEditor;
        this.quiz = quiz;
        this.task = task;
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

    public boolean isIncludeEditor() {
        return includeEditor;
    }

    public void setIncludeEditor(boolean includeEditor) {
        this.includeEditor = includeEditor;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
