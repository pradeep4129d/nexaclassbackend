package com.example.NexaClass.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Questions")
public class Questions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String type;
    @Column
    private int quizId;
    @Column
    private int taskId;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column
    private String answer;

    public Questions(int id, String type, int quizId, int taskId, String description, String answer) {
        this.id = id;
        this.type = type;
        this.quizId = quizId;
        this.taskId = taskId;
        this.description = description;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
