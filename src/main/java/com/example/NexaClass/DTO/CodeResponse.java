package com.example.NexaClass.DTO;

public class CodeResponse {
    private boolean error;
    private String output;

    public CodeResponse(boolean error, String output) {
        this.error = error;
        this.output = output;
    }

    // getters and setters
    public boolean isError() { return error; }
    public void setError(boolean error) { this.error = error; }
    public String getOutput() { return output; }
    public void setOutput(String output) { this.output = output; }
}