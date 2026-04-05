package com.example.demo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service d'envoi d'emails HTML avec OTP.
 * [POURQUOI] @Async pour ne pas bloquer le thread de la requête HTTP
 * pendant l'envoi SMTP (qui peut être lent).
 */
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.name:CodeQuest}")
    private String appName;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Envoie un email HTML contenant le code OTP pour la vérification d'email.
     */
    @Async
    public void sendOtpEmail(String toEmail, String otpCode, String pseudo) {
        String subject = appName + " — Vérifie ton adresse email";
        String htmlBody = buildOtpEmailBody(otpCode, pseudo, "vérifier ton adresse email", "10 minutes");
        sendHtmlEmail(toEmail, subject, htmlBody);
    }

    /**
     * Envoie un email HTML contenant le code OTP pour la réinitialisation du mot de passe.
     */
    @Async
    public void sendPasswordResetEmail(String toEmail, String otpCode, String pseudo) {
        String subject = appName + " — Réinitialisation du mot de passe";
        String htmlBody = buildOtpEmailBody(otpCode, pseudo, "réinitialiser ton mot de passe", "15 minutes");
        sendHtmlEmail(toEmail, subject, htmlBody);
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            // [POURQUOI] MimeMessageHelper avec utf-8 pour supporter les accents français
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true = HTML
            mailSender.send(message);
            log.info("Email envoyé avec succès à {}", to);
        } catch (MessagingException e) {
            log.error("Erreur lors de l'envoi de l'email à {}: {}", to, e.getMessage());
            throw new RuntimeException("Impossible d'envoyer l'email", e);
        }
    }

    /**
     * [POURQUOI] Template HTML inline — pas besoin de Thymeleaf pour un seul template.
     * Le design est responsive et compatible avec les principaux clients email.
     */
    private String buildOtpEmailBody(String otpCode, String pseudo, String action, String expiry) {
        return """
            <!DOCTYPE html>
            <html lang="fr">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body style="margin:0;padding:0;background-color:#0f0f23;font-family:'Segoe UI',Roboto,Arial,sans-serif;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="max-width:600px;margin:40px auto;background-color:#1a1a2e;border-radius:16px;overflow:hidden;">
                    <tr>
                        <td style="background:linear-gradient(135deg,#667eea 0%%,#764ba2 100%%);padding:40px 30px;text-align:center;">
                            <h1 style="color:#ffffff;margin:0;font-size:28px;font-weight:700;">🚀 %s</h1>
                        </td>
                    </tr>
                    <tr>
                        <td style="padding:40px 30px;">
                            <p style="color:#e0e0e0;font-size:16px;margin:0 0 20px 0;">
                                Salut <strong style="color:#667eea;">%s</strong> 👋
                            </p>
                            <p style="color:#b0b0b0;font-size:14px;margin:0 0 30px 0;">
                                Utilise ce code pour %s :
                            </p>
                            <div style="background-color:#16213e;border:2px solid #667eea;border-radius:12px;padding:20px;text-align:center;margin:0 0 30px 0;">
                                <span style="font-size:36px;font-weight:700;letter-spacing:12px;color:#ffffff;font-family:'Courier New',monospace;">
                                    %s
                                </span>
                            </div>
                            <p style="color:#888;font-size:13px;margin:0 0 10px 0;">
                                ⏱ Ce code expire dans <strong>%s</strong>.
                            </p>
                            <p style="color:#888;font-size:13px;margin:0;">
                                Si tu n'as pas fait cette demande, ignore simplement cet email.
                            </p>
                        </td>
                    </tr>
                    <tr>
                        <td style="background-color:#0f0f23;padding:20px 30px;text-align:center;">
                            <p style="color:#555;font-size:12px;margin:0;">
                                © 2024 %s — Ne réponds pas à cet email.
                            </p>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """.formatted(appName, pseudo, action, otpCode, expiry, appName);
    }
}
