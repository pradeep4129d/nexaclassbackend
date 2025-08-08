package com.example.NexaClass.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Quiz")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int facultyId;
    @Column
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(nullable = false)
    private float marksForCorrect;
    @Column(nullable = false)
    private float negativeMarks;

    public Quiz(int id, int facultyId, String title, String description, float marksForCorrect, float negativeMarks) {
        this.id = id;
        this.facultyId = facultyId;
        this.title = title;
        this.description = description;
        this.marksForCorrect = marksForCorrect;
        this.negativeMarks = negativeMarks;
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

    public float getMarksForCorrect() {
        return marksForCorrect;
    }

    public void setMarksForCorrect(int markForCorrect) {
        this.marksForCorrect = markForCorrect;
    }

    public float getNegativeMarks() {
        return negativeMarks;
    }

    public void setNegativeMarks(int negativeMarks) {
        this.negativeMarks = negativeMarks;
    }
}
