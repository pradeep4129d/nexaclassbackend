package com.example.NexaClass.DTO;

public class OutputMessage {
    private String processId;
    private boolean error;
    private String text;
    private boolean finished;
    private boolean newProcess;
    public OutputMessage() {}

    public OutputMessage(String executionId, boolean error, String text, boolean finished,boolean newProcess) {
        this.processId = executionId;
        this.error = error;
        this.text = text;
        this.finished = finished;
        this.newProcess=newProcess;
    }

    public boolean isNewProcess() {
        return newProcess;
    }

    public void setNewProcess(boolean newProcess) {
        this.newProcess = newProcess;
    }

    // getters/setters
    public String getProcessId() { return processId; }
    public void setProcessId(String processId) { this.processId = processId; }
    public boolean isError() { return error; }
    public void setError(boolean error) { this.error = error; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public boolean isFinished() { return finished; }
    public void setFinished(boolean finished) { this.finished = finished; }
}