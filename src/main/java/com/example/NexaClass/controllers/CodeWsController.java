package com.example.NexaClass.controllers;

import com.example.NexaClass.DTO.InputRequest;
import com.example.NexaClass.DTO.RunRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Controller
public class CodeWsController {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public CodeWsController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    private final Map<String, Process> processes = new ConcurrentHashMap<>();
    private final Map<String, String> userProcesses = new ConcurrentHashMap<>();
    private String processTempDir() {
        return System.getProperty("java.io.tmpdir");
    }
    private Path writeCodeToFile(String language, String code) throws IOException{
        String fileName;
        switch (language.toLowerCase()) {
            case "java": fileName = "Main.java"; break;
            case "python": fileName = "script.py"; break;
            case "c": fileName = "program.c"; break;
            case "cpp": fileName = "program.cpp"; break;
            case "js": fileName = "script.js"; break;
            default: fileName="example.txt";
        }
        Path p = Path.of(processTempDir(), UUID.randomUUID() + "-" + fileName);
        Files.createDirectories(p.getParent());
        Files.writeString(p, code);
        return p;
    }
    private String[] buildCommand(String language, Path file) {
        switch (language.toLowerCase()) {
            case "java":
                Path parent = file.getParent();
                String compileJava = "javac \"" + file.toString() + "\"";
                String runJava = "java -cp \"" + parent.toString() + "\" Main";
                return new String[]{"cmd.exe", "/c", compileJava + " && " + runJava};
            case "python":
                return new String[]{"python", file.toString()};
            case "c":
                String exeName = UUID.randomUUID() + "-program.exe";
                Path cExe = file.getParent().resolve(exeName);
                String compileC = "gcc \"" + file.toString() + "\" -o \"" + cExe.toString() + "\"";
                String runC = "\"" + cExe.toString() + "\"";
                return new String[]{"cmd.exe", "/c", compileC + " && " + runC};
            case "cpp":
            case "c++":
                Path cppExe = file.getParent().resolve("program.exe");
                String compileCpp = "g++ \"" + file.toString() + "\" -o \"" + cppExe.toString() + "\"";
                String runCpp = "\"" + cppExe.toString() + "\"";
                return new String[]{"cmd.exe", "/c", compileCpp + " && " + runCpp};
            case "javascript":
                return new String[]{"node", file.toString()};
            default:
                throw new IllegalArgumentException("Unsupported language: " + language);
        }
    }
    private void stopProcess(Process process) {
        if (process != null) {
            process.destroy();
            try {
                if (!process.waitFor(2, TimeUnit.SECONDS)) {
                    process.destroyForcibly();
                }
            } catch (InterruptedException e) {
                process.destroyForcibly();
                Thread.currentThread().interrupt();
            }
        }
    }
    @MessageMapping("/run")
    @SendTo("/topic/output")
    public void runCode(RunRequest request) {
        String studentId = request.getStudentId();
        if (userProcesses.containsKey(studentId)) {
            String oldProcessId = userProcesses.get(studentId);
            Process oldProcess = processes.remove(oldProcessId);
            stopProcess(oldProcess);
            userProcesses.remove(studentId);
        }
        String processId = UUID.randomUUID().toString();
        try {
            Path file = writeCodeToFile(request.getLanguage(), request.getCode());
            String[] cmd = buildCommand(request.getLanguage(), file);
            Process process = new ProcessBuilder(cmd)
                    .redirectErrorStream(false)
                    .start();
            processes.put(processId,process);
            userProcesses.put(studentId, processId);
            System.out.println(processes.size());
            messagingTemplate.convertAndSend("/topic/output/"+request.getStudentId(),Map.of("processId",processId));
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                        messagingTemplate.convertAndSend("/topic/output/" + processId, Map.of("type","stdout","line", line));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                        messagingTemplate.convertAndSend("/topic/output/" + processId,
                                Map.of("type", "stderr", "line", line));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @MessageMapping("/input")
    public void sendInput(InputRequest inputRequest) {
        String processId = inputRequest.getProcessId();
        String input = inputRequest.getInput();
        Process process = processes.get(processId);
        if (process == null) {
            System.out.println("No process found with id: " + processId);
            return;
        }
        try {
            OutputStream stdin = process.getOutputStream();
            stdin.write((input + "\n").getBytes());
            stdin.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
