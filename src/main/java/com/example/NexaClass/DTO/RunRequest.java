package com.example.NexaClass.DTO;

public class RunRequest {
    private String language;
    private String code;
    private String OldProcessId;
    private String studentId;
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getOldProcessId() {
        return OldProcessId;
    }

    public void setOldProcessId(String oldProcessId) {
        OldProcessId = oldProcessId;
    }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}