package com.example.NexaClass.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Options")
public class Options {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int questionId;
    @Column(nullable = false,columnDefinition = "TEXT")
    private String description;

    public Options(){}
    public Options(int id, int questionId, String description) {
        this.id = id;
        this.questionId = questionId;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
