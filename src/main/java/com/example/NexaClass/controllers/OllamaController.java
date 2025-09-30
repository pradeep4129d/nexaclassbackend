package com.example.NexaClass.controllers;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatbot")
@CrossOrigin("*")
public class OllamaController {

    private final ChatClient chatClient;

    public OllamaController(ChatClient chatClient){
        this.chatClient = chatClient;
    }

    public record ChatRequest(String prompt) {}
    public record ChatResponse(String answer) {}

    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestBody ChatRequest chatRequest){
        System.out.println("Prompt: " + chatRequest.prompt());
        String res = chatClient.prompt()
                .user(chatRequest.prompt())
                .call()
                .content();
        return ResponseEntity.ok(new ChatResponse(res));
    }
}

