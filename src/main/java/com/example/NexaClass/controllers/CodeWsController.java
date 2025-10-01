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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class CodeWsController {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public CodeWsController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    HashMap<String,Process>processes=new HashMap<>();
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
                Path cExe = file.getParent().resolve("program.exe");
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
    @MessageMapping("/run")
    @SendTo("/topic/output")
    public ResponseEntity<?> runCode(RunRequest request) {
        if(!request.getOldProcessId().isEmpty()){
            processes.remove(request.getOldProcessId());
        }
        String processId = UUID.randomUUID().toString();
        try {
            Path file = writeCodeToFile(request.getLanguage(), request.getCode());
            String[] cmd = buildCommand(request.getLanguage(), file);
            Process process = new ProcessBuilder(cmd)
                    .redirectErrorStream(false)
                    .start();
            processes.put(processId,process);
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
        return ResponseEntity.ok("successful");
    }
    @MessageMapping("/input")
    @SendTo("/topic/output")
    public String sendInput(InputRequest inputRequest) {
        System.out.println("Received input:");
        System.out.println(inputRequest.getInput());
        return "Input received: " + inputRequest.getInput();
    }
}
