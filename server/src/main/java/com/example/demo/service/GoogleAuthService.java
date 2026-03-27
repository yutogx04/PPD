package com.example.demo.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

/**
 * Service de vérification des tokens Google Identity.
 * [POURQUOI] Vérifie le idToken côté serveur pour garantir l'authenticité
 * de la connexion Google — ne jamais faire confiance au client uniquement.
 */
@Service
public class GoogleAuthService {

    private static final Logger log = LoggerFactory.getLogger(GoogleAuthService.class);

    @Value("${google.client-id}")
    private String googleClientId;

    private GoogleIdTokenVerifier verifier;

    @PostConstruct
    public void init() {
        // [POURQUOI] Le verifier est initialisé une seule fois au démarrage
        // avec le Web Client ID (pas l'Android Client ID)
        this.verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance()
        )
                .setAudience(Collections.singletonList(googleClientId))
                .build();
    }

    /**
     * Vérifie un Google ID token et retourne les informations utilisateur.
     *
     * @param idTokenString Le token reçu du client
     * @return GoogleUserInfo contenant email, nom, sub (Google user ID)
     * @throws IllegalArgumentException si le token est invalide
     */
    public GoogleUserInfo verifyIdToken(String idTokenString) {
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new IllegalArgumentException("Token Google invalide ou expiré");
            }

            Payload payload = idToken.getPayload();

            // [POURQUOI] On vérifie que l'email est vérifié côté Google
            // pour éviter les comptes Google non confirmés
            if (!Boolean.TRUE.equals(payload.getEmailVerified())) {
                throw new IllegalArgumentException("L'email Google n'est pas vérifié");
            }

            return new GoogleUserInfo(
                    payload.getEmail(),
                    (String) payload.get("name"),
                    payload.getSubject(), // [POURQUOI] Le "sub" est l'identifiant unique Google
                    (String) payload.get("picture")
            );

        } catch (GeneralSecurityException | IOException e) {
            log.error("Erreur lors de la vérification du token Google: {}", e.getMessage());
            throw new IllegalArgumentException("Impossible de vérifier le token Google", e);
        }
    }

    /**
     * Record pour encapsuler les informations utilisateur Google.
     */
    public record GoogleUserInfo(
            String email,
            String name,
            String googleId,
            String pictureUrl
    ) {}
}
