package com.example.NexaClass.DTO;

import com.example.NexaClass.entities.Options;
import com.example.NexaClass.entities.Questions;

import java.util.List;

public class QuizQuestionsDTO {
    private Questions questions;
    private List<Options>options;

    public QuizQuestionsDTO(Questions questions, List<Options> options) {
        this.questions = questions;
        this.options = options;
    }

    public Questions getQuestions() {
        return questions;
    }

    public void setQuestions(Questions questions) {
        this.questions = questions;
    }

    public List<Options> getOptions() {
        return options;
    }

    public void setOptions(List<Options> options) {
        this.options = options;
    }
}
