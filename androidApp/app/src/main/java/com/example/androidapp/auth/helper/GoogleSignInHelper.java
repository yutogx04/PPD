package com.example.androidapp.auth.helper;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

/**
 * Wrapper autour de GoogleSignInClient pour simplifier l'intégration.
 * [POURQUOI] Encapsule la configuration et le parsing du résultat
 * pour garder les Activities propres.
 */
public class GoogleSignInHelper {

    // [POURQUOI] WEB_CLIENT_ID (pas Android Client ID) — c'est le même ID
    // que celui configuré côté backend dans application.yml
    // Doit correspondre à Google Cloud Console → OAuth 2.0 → Web Application
    public static final String WEB_CLIENT_ID = "YOUR_GOOGLE_WEB_CLIENT_ID.apps.googleusercontent.com";

    public static final int RC_SIGN_IN = 9001;

    private final GoogleSignInClient googleSignInClient;

    public GoogleSignInHelper(Context context) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // [POURQUOI] requestIdToken avec le WEB client ID pour obtenir
                // un idToken vérifiable côté serveur
                .requestIdToken(WEB_CLIENT_ID)
                .requestEmail()
                .requestProfile()
                .build();

        this.googleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    /**
     * Retourne l'Intent pour lancer le flow Google Sign-In.
     * Usage : startActivityForResult(getSignInIntent(), RC_SIGN_IN)
     */
    public Intent getSignInIntent() {
        return googleSignInClient.getSignInIntent();
    }

    /**
     * Parse le résultat de onActivityResult et extrait le idToken.
     *
     * @param data L'Intent reçu dans onActivityResult
     * @return Le idToken Google ou null si échec
     */
    public String handleSignInResult(Intent data) {
        try {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account = task.getResult(ApiException.class);

            if (account != null && account.getIdToken() != null) {
                return account.getIdToken();
            }
        } catch (ApiException e) {
            // [POURQUOI] ApiException contient les codes d'erreur Google
            // (ex: 12501 = annulé par l'utilisateur, 12500 = erreur interne)
            android.util.Log.e("GoogleSignIn", "Code erreur: " + e.getStatusCode(), e);
        }
        return null;
    }

    /**
     * Déconnexion Google (à appeler lors du logout).
     */
    public void signOut() {
        googleSignInClient.signOut();
    }

    /**
     * Révocation complète de l'accès Google (l'utilisateur devra
     * re-autoriser l'app à la prochaine connexion).
     */
    public void revokeAccess() {
        googleSignInClient.revokeAccess();
    }
}
