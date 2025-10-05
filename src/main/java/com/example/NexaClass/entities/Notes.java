package com.example.NexaClass.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "notes")
public class Notes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Long studentId;
    @Column(nullable = false)
    private String note;
    @Column
    private String content;
    public Notes(){}

    public Notes(Integer id, Long studentId, String note, String content) {
        this.id = id;
        this.studentId = studentId;
        this.note = note;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudent_id(Long studentId) {
        this.studentId = studentId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
