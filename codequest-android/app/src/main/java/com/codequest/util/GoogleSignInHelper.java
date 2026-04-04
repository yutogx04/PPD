package com.codequest.util;

import android.app.Activity;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;

import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;

import java.util.concurrent.Executors;

public class GoogleSignInHelper {

    private static final String TAG = "GoogleSignInHelper";

    private static final String WEB_CLIENT_ID =
            "547784745033-011un48ufm05cnch3j7vcd21281pnb83.apps.googleusercontent.com";

    public interface GoogleSignInCallback {
        void onSuccess(String idToken);
        void onFailure(String errorMessage);
    }

    public static void signIn(Activity activity, GoogleSignInCallback callback) {
        CredentialManager credentialManager = CredentialManager.create(activity);

        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)   
                .setServerClientId(WEB_CLIENT_ID)
                .build();

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        CancellationSignal cancellationSignal = new CancellationSignal();

        credentialManager.getCredentialAsync(
                activity,
                request,
                cancellationSignal,
                Executors.newSingleThreadExecutor(),
                new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse result) {
                        handleSignIn(result, callback);
                    }

                    @Override
                    public void onError(GetCredentialException e) {
                        Log.e(TAG, "Google Sign-In failed", e);
                        activity.runOnUiThread(() -> {
                            String msg = e.getMessage() != null && e.getMessage().contains("No credentials") ?
                                    "Google Sign-In bloqué par sécurité. Enregistrez l'empreinte SHA-1/SHA-256 de votre PC dans Firebase." :
                                    "Connexion Google échouée: " + e.getMessage();
                            callback.onFailure(msg);
                        });
                    }
                }
        );
    }

    private static void handleSignIn(GetCredentialResponse response, GoogleSignInCallback callback) {
        Credential credential = response.getCredential();

        if (credential instanceof CustomCredential) {
            CustomCredential customCredential = (CustomCredential) credential;
            if (GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                    .equals(customCredential.getType())) {
                Bundle data = customCredential.getData();
                GoogleIdTokenCredential googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(data);
                String googleIdToken = googleIdTokenCredential.getIdToken();
                Log.d(TAG, "Google ID token obtained, converting to Firebase ID token...");

                // Convert Google ID token -> Firebase ID token
                com.google.firebase.auth.AuthCredential firebaseCredential =
                        com.google.firebase.auth.GoogleAuthProvider.getCredential(googleIdToken, null);
                com.google.firebase.auth.FirebaseAuth.getInstance()
                        .signInWithCredential(firebaseCredential)
                        .addOnSuccessListener(authResult -> {
                            com.google.firebase.auth.FirebaseUser firebaseUser = authResult.getUser();
                            if (firebaseUser != null) {
                                firebaseUser.getIdToken(true)
                                        .addOnSuccessListener(tokenResult -> {
                                            String firebaseIdToken = tokenResult.getToken();
                                            Log.d(TAG, "Firebase ID token obtained successfully");
                                            callback.onSuccess(firebaseIdToken);
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e(TAG, "Failed to get Firebase ID token", e);
                                            callback.onFailure("Échec d'obtention du token Firebase: " + e.getMessage());
                                        });
                            } else {
                                callback.onFailure("Utilisateur Firebase null");
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Firebase signInWithCredential failed", e);
                            callback.onFailure("Échec Firebase Auth: " + e.getMessage());
                        });
            } else {
                Log.w(TAG, "Unexpected credential type: " + customCredential.getType());
                callback.onFailure("Type de credential inattendu");
            }
        } else {
            Log.w(TAG, "Credential is not a CustomCredential");
            callback.onFailure("Type de credential non supporté");
        }
    }
}
