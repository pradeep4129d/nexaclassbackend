package com.example.NexaClass.services;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Service
public class CodeExecutionService {
    private static final long TIMEOUT_SECONDS = 10; // max execution time
    public String executeCode(String language, String code) throws IOException, InterruptedException {
        String fileName;
        String[] cmd;
        switch (language.toLowerCase()) {
            case "java":
                fileName = "Main.java";
                Files.writeString(Path.of(fileName), code);
                // compile
                Process compileJava = new ProcessBuilder("javac", fileName).start();
                if (!compileJava.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                    compileJava.destroyForcibly();
                    return "Compilation timed out";
                }
                if (compileJava.exitValue() != 0) {
                    return readStream(compileJava.getErrorStream());
                }
                cmd = new String[]{"java", "Main"};
                break;

            case "python":
                fileName = "script.py";
                Files.writeString(Path.of(fileName), code);
                cmd = new String[]{"python3", fileName};
                break;

            case "c":
                fileName = "program.c";
                Files.writeString(Path.of(fileName), code);
                Process compileC = new ProcessBuilder("gcc", fileName, "-o", "program.exe").start();
                if (!compileC.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                    compileC.destroyForcibly();
                    return "Compilation timed out";
                }
                if (compileC.exitValue() != 0) {
                    return readStream(compileC.getErrorStream());
                }
                cmd = new String[]{"program.exe"};
                break;

            case "cpp":
            case "c++":
                fileName = "program.cpp";
                Files.writeString(Path.of(fileName), code);
                Process compileCpp = new ProcessBuilder("g++", fileName, "-o", "program.exe").start();
                if (!compileCpp.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                    compileCpp.destroyForcibly();
                    return "Compilation timed out";
                }
                if (compileCpp.exitValue() != 0) {
                    return readStream(compileCpp.getErrorStream());
                }
                cmd = new String[]{"program.exe"};
                break;

            case "javascript":
            case "js":
                fileName = "script.js";
                Files.writeString(Path.of(fileName), code);
                cmd = new String[]{"node", fileName};
                break;

            default:
                throw new IllegalArgumentException("Unsupported language: " + language);
        }

        Process process = new ProcessBuilder(cmd).start();

        // wait for execution with timeout
        if (!process.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
            process.destroyForcibly();
            return "Execution timed out";
        }

        String output = readStream(process.getInputStream());
        String errors = readStream(process.getErrorStream());

        return errors.isEmpty() ? output : errors;
    }

    private String readStream(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString().trim();
        }
    }
}

