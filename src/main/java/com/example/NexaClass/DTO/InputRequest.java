package com.example.NexaClass.DTO;

public class InputRequest {
    private String input;
    private String processId;

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    // getters/setters
    public String getInput() { return input; }
    public void setInput(String input) { this.input = input; }
}