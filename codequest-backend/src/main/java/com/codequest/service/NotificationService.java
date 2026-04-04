package com.codequest.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.codequest.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    public void sendToUser(User user, String title, String body, String type) {
        if (user.getFcmToken() == null || user.getFcmToken().isEmpty()) {
            return;
        }
        try {
            Message message = Message.builder()
                    .setToken(user.getFcmToken())
                    .putData("type", type)
                    .putData("title", title)
                    .putData("body", body)
                    .build();
            FirebaseMessaging.getInstance().send(message);
            log.info("Notification sent to user {}: {}", user.getPseudo(), title);
        } catch (Exception e) {
            log.warn("Failed to send notification to user {}: {}", user.getPseudo(), e.getMessage());
        }
    }
}
