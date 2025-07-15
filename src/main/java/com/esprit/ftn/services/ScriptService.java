package com.esprit.ftn.services;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
public class ScriptService {

    public void runScript() throws Exception {
        // Path to your Python launcher
        String pythonPath = "C:\\Program Files\\Python\\Python313\\python.exe";

        // Script and args
        String command = pythonPath + " ftnatation_extractor.py" +
                " --base-url http://ftnatation.tn/eau-libre-resultats/" +
                " --download-dir ./pdfs" +
                " --json-dir ./json";

        // Script working directory (where the Python script is)
        File workingDir = new File("./scripts");

        // Execute the command
        Process process = Runtime.getRuntime().exec(command, null, workingDir);

        // Read standard output (stdout) using UTF-8 encoding
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            System.out.println("---- Script Output ----");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        // Read error output (stderr) using UTF-8 encoding
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8))) {
            String line;
            System.err.println("---- Script Error Output ----");
            while ((line = reader.readLine()) != null) {
                System.err.println(line);
            }
        }

        // Wait for the script to finish and check the exit code
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Python script exited with code " + exitCode);
        }

        System.out.println("Python script completed successfully.");
    }
}
