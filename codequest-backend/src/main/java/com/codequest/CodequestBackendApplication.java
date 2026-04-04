package com.codequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CodequestBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodequestBackendApplication.class, args);
    }
}
