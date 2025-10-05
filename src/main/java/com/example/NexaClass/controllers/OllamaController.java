package com.example.NexaClass.controllers;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chatbot")
@CrossOrigin("*")
public class OllamaController {

    private final ChatClient chatClient;
    public OllamaController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }
    public static class QuestionData {
        public String question;
        public String key;
        public String studentAnswer;
        public int maxMarks;
    }
    public static class EvaluateRequest {
        public List<QuestionData> questions;
    }
    public static class EvaluateResponse {
        public int totalMarks;
        public EvaluateResponse(int totalMarks) {
            this.totalMarks = totalMarks;
        }
    }
    @PostMapping("/evaluate")
    public ResponseEntity<EvaluateResponse> evaluate(@RequestBody EvaluateRequest request) {
        int totalMarks = 0;
        for (QuestionData q : request.questions) {
            String prompt = """
                    You are an unbiased and strict evaluator for computer science theory answers.
                    Your task is to evaluate a student answer against the question and the answer key, and return only the marks scored as a number.
                    Do NOT output any explanation, text, punctuation, or extra characters — just the number.
                    
                    Follow these rules strictly:
                    1. If the student answer is exactly correct, give full marks.
                    2. If the student answer is partially correct, give marks proportional to correctness.
                    3. If the student answer is completely wrong or irrelevant or empty, give 0 marks.
                    4. Do not be lenient or generous for unrelated answers.
                    5. Ignore spelling, grammar, and formatting differences — focus only on correctness and completeness.
                    6. Always return a number, nothing else.
                    
                    Question: "%s"
                    Answer Key: "%s"
                    Student Answer: "%s"
                    Maximum Marks: %d
                    """.formatted(q.question, q.key, q.studentAnswer, q.maxMarks);
            try {
                String res = chatClient.prompt()
                        .user(prompt)
                        .call()
                        .content()
                        .trim();
                int marks = 0;
                try {
                    marks = Integer.parseInt(res);
                    System.out.println(marks);
                } catch (NumberFormatException e) {
                    marks = 0;
                }
                totalMarks += marks;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok(new EvaluateResponse(totalMarks));
    }
    @PostMapping("/chat")
    public Map<String, String> getChatResponse(@RequestBody Map<String, String> request) {
        String question = request.get("query");
        String systemPrompt = """
                You are an academic assistant for college students.
                Your purpose:
                - Explain concepts clearly and briefly.
                - Clarify doubts and guide debugging.
                - You may give *short example code snippets (≤5 lines)* only when absolutely necessary.
                - Never give full working code, full programs, or complete answers.
                - Keep every response under 10 concise lines.
                - Prefer bullet points or numbered steps.
                - Encourage the student to reason and find the fix themselves.
                - Maintain a friendly, supportive tone.
                """;
        String response = chatClient.prompt()
                .system(systemPrompt)
                .user(question)
                .call()
                .content();
        return Map.of("response", response);
    }
}
