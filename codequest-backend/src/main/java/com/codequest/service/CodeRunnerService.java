package com.codequest.service;

import com.codequest.entity.AppSettings;
import com.codequest.repository.AppSettingsRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CodeRunnerService {

    @Value("${app.docker.python-image}")
    private String pythonImage;

    @Value("${app.docker.node-image}")
    private String nodeImage;

    @Value("${app.docker.java-image}")
    private String javaImage;

    @Value("${app.docker.timeout-seconds:5}")
    private int defaultTimeoutSeconds;

    @Value("${app.docker.memory-limit-mb:64}")
    private int defaultMemoryLimitMb;

    private final AppSettingsRepository appSettingsRepository;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ExecutionResult {
        private String stdout;
        private String stderr;
        private int exitCode;
        private long executionTimeMs;
        private boolean timedOut;
    }

    public ExecutionResult execute(String code, String language, String input) {
        
        AppSettings settings = appSettingsRepository.findAll().stream().findFirst().orElse(null);
        int timeoutSeconds = (settings != null) ? settings.getSandboxTimeoutSec() : defaultTimeoutSeconds;
        int memoryLimitMb  = (settings != null) ? settings.getSandboxMemoryMb()  : defaultMemoryLimitMb;

        Path tempDir = null;
        try {
            tempDir = Files.createTempDirectory("codequest-sandbox-");

            String filename;
            String image;
            if ("PYTHON".equalsIgnoreCase(language)) {
                filename = "solution.py";
                image = pythonImage;
            } else if ("JAVASCRIPT".equalsIgnoreCase(language)) {
                filename = "solution.js";
                image = nodeImage;
            } else if ("JAVA".equalsIgnoreCase(language)) {
                filename = "solution.java";
                image = javaImage;
            } else {
                return ExecutionResult.builder()
                        .stderr("Langage non supporté: " + language)
                        .exitCode(1)
                        .build();
            }

            Path solutionFile = tempDir.resolve(filename);
            Files.writeString(solutionFile, code);

            Path inputFile = tempDir.resolve("input.txt");
            Files.writeString(inputFile, input != null ? input : "");

            ProcessBuilder pb = new ProcessBuilder(
                    "docker", "run",
                    "--rm",
                    "--network", "none",
                    "--memory", memoryLimitMb + "m",
                    "--memory-swap", memoryLimitMb + "m",
                    "--cpus", "0.5",
                    "--pids-limit", "32",
                    "--read-only",
                    "--tmpfs", "/tmp:size=16m",
                    "-v", tempDir.toAbsolutePath() + ":/sandbox:ro",
                    "-w", "/sandbox",
                    "-i",
                    image
            );

            pb.redirectErrorStream(false);

            long startTime = System.currentTimeMillis();
            Process process = pb.start();

            if (input != null && !input.isEmpty()) {
                try (OutputStream os = process.getOutputStream()) {
                    os.write(input.getBytes());
                    os.flush();
                }
            }

            ExecutorService executor = Executors.newFixedThreadPool(2);
            Future<String> stdoutFuture = executor.submit(() -> readStream(process.getInputStream()));
            Future<String> stderrFuture = executor.submit(() -> readStream(process.getErrorStream()));

            boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
            long executionTimeMs = System.currentTimeMillis() - startTime;

            if (!finished) {
                process.destroyForcibly();
                executor.shutdownNow();
                return ExecutionResult.builder()
                        .stderr("Temps d'exécution dépassé (limite: " + timeoutSeconds + "s)")
                        .exitCode(-1)
                        .executionTimeMs(executionTimeMs)
                        .timedOut(true)
                        .build();
            }

            String stdout = stdoutFuture.get(2, TimeUnit.SECONDS);
            String stderr = stderrFuture.get(2, TimeUnit.SECONDS);
            executor.shutdown();

            return ExecutionResult.builder()
                    .stdout(stdout)
                    .stderr(stderr)
                    .exitCode(process.exitValue())
                    .executionTimeMs(executionTimeMs)
                    .timedOut(false)
                    .build();

        } catch (Exception e) {
            log.error("Code execution failed: {}", e.getMessage(), e);
            return ExecutionResult.builder()
                    .stderr("Erreur interne du serveur: " + e.getMessage())
                    .exitCode(-1)
                    .build();
        } finally {
            if (tempDir != null) {
                cleanupTempDir(tempDir);
            }
        }
    }

    private String readStream(InputStream stream) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (sb.length() > 0) sb.append("\n");
                sb.append(line);
            }
        }
        return sb.toString();
    }

    private void cleanupTempDir(Path dir) {
        try {
            Files.walk(dir)
                    .sorted((a, b) -> b.compareTo(a))
                    .forEach(path -> {
                        try { Files.deleteIfExists(path); } catch (IOException ignored) {}
                    });
        } catch (IOException e) {
            log.warn("Failed to cleanup temp dir: {}", dir, e);
        }
    }
}
