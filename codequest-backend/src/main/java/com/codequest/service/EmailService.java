package com.codequest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOtpEmail(String to, String otp) {
        log.info("===== PREPARING OTP EMAIL for {} =====", to);
        new Thread(() -> {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(fromEmail);
                message.setTo(to);
                message.setSubject("CodeQuest — Vérification de votre email");
                message.setText(
                    "Bienvenue sur CodeQuest !\n\n" +
                    "Votre code de vérification est : " + otp + "\n\n" +
                    "Ce code expire dans 5 minutes.\n\n" +
                    "Si vous n'avez pas créé de compte, ignorez cet email."
                );
                mailSender.send(message);
                log.info("===== OTP EMAIL SENT SUCCESSFULLY to {} =====", to);
            } catch (Exception e) {
                log.error("===== FAILED TO SEND OTP EMAIL to {} =====", to);
                log.error("===== EMAIL ERROR: {} =====", e.getMessage());
                log.warn("===== [DEV BYPASS] Email delivery failed. Manual OTP Code: {} for {} =====", otp, to);
            }
        }).start();
    }

    public void sendPasswordResetEmail(String to, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject("CodeQuest — Réinitialisation du mot de passe");
            message.setText(
                "Vous avez demandé la réinitialisation de votre mot de passe.\n\n" +
                "Votre code de vérification est : " + otp + "\n\n" +
                "Ce code expire dans 5 minutes.\n\n" +
                "Si vous n'êtes pas à l'origine de cette demande, ignorez cet email."
            );
            mailSender.send(message);
            log.info("Password reset email sent to {}", to);
        } catch (Exception e) {
            log.error("Failed to send reset email to {}: {}", to, e.getMessage());
        }
    }
}
