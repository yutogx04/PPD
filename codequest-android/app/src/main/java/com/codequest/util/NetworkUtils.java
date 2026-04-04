package com.codequest.util;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Handler;
import android.os.Looper;
public class NetworkUtils {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        Network activeNetwork = cm.getActiveNetwork();
        if (activeNetwork == null) return false;
        NetworkCapabilities caps = cm.getNetworkCapabilities(activeNetwork);
        if (caps == null) return false;
        return caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
    }
    public static void retryWithBackoff(Runnable action, int maxRetries, long initialDelayMs) {
        retryInternal(action, maxRetries, initialDelayMs, 0);
    }
    private static void retryInternal(Runnable action, int maxRetries, long delayMs, int attempt) {
        Handler handler = new Handler(Looper.getMainLooper());
        try {
            action.run();
        } catch (Exception e) {
            if (attempt < maxRetries) {
                handler.postDelayed(() ->
                        retryInternal(action, maxRetries, delayMs * 2, attempt + 1),
                        delayMs);
            }
        }
    }
    public interface NetworkAction {
        void onAvailable();
        void onUnavailable();
    }
    public static void executeIfOnline(Context context, NetworkAction action) {
        if (isNetworkAvailable(context)) {
            action.onAvailable();
        } else {
            action.onUnavailable();
        }
    }
}
