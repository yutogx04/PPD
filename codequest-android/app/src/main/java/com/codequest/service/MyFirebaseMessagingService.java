package com.codequest.service;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.codequest.R;
import com.codequest.ui.main.MainActivity;
import com.codequest.util.SharedPrefManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "codequest_notifications";
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        
        SharedPrefManager prefs = SharedPrefManager.getInstance(this);
        if (prefs.isLoggedIn()) {
            try {
                java.util.Map<String, String> body = new java.util.HashMap<>();
                body.put("token", token);
                com.codequest.network.RetrofitClient.getApi()
                        .registerFcmToken(body).enqueue(new retrofit2.Callback<Void>() {
                    @Override
                    public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {}
                    @Override
                    public void onFailure(retrofit2.Call<Void> call, Throwable t) {}
                });
            } catch (Exception ignored) {}
        }
    }
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        String type = message.getData().get("type");
        String title = message.getData().get("title");
        String body = message.getData().get("body");
        if (type == null || title == null || body == null) {
            if (message.getNotification() != null) {
                title = message.getNotification().getTitle();
                body = message.getNotification().getBody();
                type = "general";
            } else {
                return;
            }
        }
        SharedPrefManager prefs = SharedPrefManager.getInstance(this);
        if (!shouldShowNotification(prefs, type)) return;
        showNotification(title, body);
    }
    private boolean shouldShowNotification(SharedPrefManager prefs, String type) {
        switch (type) {
            case "daily_reminder":    return prefs.isNotifDaily();
            case "streak_alert":      return prefs.isNotifStreak();
            case "badge_unlocked":    return prefs.isNotifBadge();
            case "friend_request":    return prefs.isNotifFriend();
            case "daily_challenge":   return prefs.isNotifDailyChallenge();
            case "mutual_challenge":  return prefs.isNotifMutual();
            default:                  return true;
        }
    }
    private void showNotification(String title, String body) {
        createNotificationChannel();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify((int) System.currentTimeMillis(), builder.build());
        }
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "CodeQuest Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifications pour rappels, badges, amis et défis");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}
