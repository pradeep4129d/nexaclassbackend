package com.example.NexaClass.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "ClassMember")
public class ClassMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private int studentId;
    @Column(nullable = false, unique = true)
    private int classRoomId;
    public ClassMember(){};
    public ClassMember(int id, int studentId, int classRoomId) {
        this.id = id;
        this.studentId = studentId;
        this.classRoomId = classRoomId;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getClassRoomId() {
        return classRoomId;
    }

    public void setClassRoomId(int classRoomId) {
        this.classRoomId = classRoomId;
    }
}
