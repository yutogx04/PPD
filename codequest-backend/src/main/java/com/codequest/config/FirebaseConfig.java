package com.codequest.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@Slf4j
public class FirebaseConfig {

    @PostConstruct
    public void initFirebase() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                InputStream serviceAccount =
                        new ClassPathResource("firebase-service-account.json").getInputStream();

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);
                log.info("===== Firebase Admin SDK initialized successfully =====");
            }
        } catch (IOException e) {
            log.error("===== Failed to initialize Firebase Admin SDK =====", e);
            throw new RuntimeException("Cannot initialize Firebase", e);
        }
    }
}
