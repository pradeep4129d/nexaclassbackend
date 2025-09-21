package com.example.NexaClass.DTO;

import com.example.NexaClass.entities.Options;

import java.util.List;

public class QuestionDTO {
    private int id;
    private int quizId;
    private int taskId;
    private String description;
    private String answer;
    private List<Options>options;
    public QuestionDTO(){

    }
    public QuestionDTO(int quizId, int taskId, String description, String answer) {
        this.quizId = quizId;
        this.taskId = taskId;
        this.description = description;
        this.answer = answer;
    }
    public QuestionDTO(int quizId, int taskId, String description, String answer, List<Options> options) {
        this.quizId = quizId;
        this.taskId = taskId;
        this.description = description;
        this.answer = answer;
        this.options = options;
    }

    public QuestionDTO(int id, int quizId, int taskId, String description, String answer, List<Options> options) {
        this.id = id;
        this.quizId = quizId;
        this.taskId = taskId;
        this.description = description;
        this.answer = answer;
        this.options = options;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<Options> getOptions() {
        return options;
    }

    public void setOptions(List<Options> options) {
        this.options = options;
    }
}
