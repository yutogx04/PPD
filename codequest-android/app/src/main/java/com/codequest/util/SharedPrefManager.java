package com.codequest.util;
import android.content.Context;
import android.content.SharedPreferences;
public class SharedPrefManager {
    private static final String PREF_NAME = "codequest_prefs";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_PSEUDO = "user_pseudo";
    private static final String KEY_USER_XP = "user_xp";
    private static final String KEY_USER_LEVEL = "user_level";
    private static final String KEY_USER_STREAK = "user_streak";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_ONBOARDING_DONE = "onboarding_done";
    private static final String KEY_DARK_MODE = "dark_mode";
    private static final String KEY_DAILY_REMINDER_HOUR = "daily_reminder_hour";
    private static final String KEY_NOTIF_DAILY = "notif_daily";
    private static final String KEY_NOTIF_STREAK = "notif_streak";
    private static final String KEY_NOTIF_BADGE = "notif_badge";
    private static final String KEY_NOTIF_FRIEND = "notif_friend";
    private static final String KEY_NOTIF_DAILY_CHALLENGE = "notif_daily_challenge";
    private static final String KEY_NOTIF_MUTUAL = "notif_mutual";
    private static final String KEY_LANGUAGE = "app_language";
    private static SharedPrefManager instance;
    private final SharedPreferences prefs;
    private SharedPrefManager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }
    public void saveTokens(String accessToken, String refreshToken) {
        prefs.edit()
                .putString(KEY_ACCESS_TOKEN, accessToken)
                .putString(KEY_REFRESH_TOKEN, refreshToken)
                .putBoolean(KEY_IS_LOGGED_IN, true)
                .apply();
    }
    public String getAccessToken() {
        return prefs.getString(KEY_ACCESS_TOKEN, null);
    }
    public String getRefreshToken() {
        return prefs.getString(KEY_REFRESH_TOKEN, null);
    }
    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    public void saveUserInfo(long userId, String pseudo, int xp, int level, int streak) {
        prefs.edit()
                .putLong(KEY_USER_ID, userId)
                .putString(KEY_USER_PSEUDO, pseudo)
                .putInt(KEY_USER_XP, xp)
                .putInt(KEY_USER_LEVEL, level)
                .putInt(KEY_USER_STREAK, streak)
                .apply();
    }
    public long getUserId() {
        return prefs.getLong(KEY_USER_ID, -1);
    }
    public String getUserPseudo() {
        return prefs.getString(KEY_USER_PSEUDO, "");
    }
    public int getUserXp() {
        return prefs.getInt(KEY_USER_XP, 0);
    }
    public int getUserLevel() {
        return prefs.getInt(KEY_USER_LEVEL, 1);
    }
    public int getUserStreak() {
        return prefs.getInt(KEY_USER_STREAK, 0);
    }
    public void setOnboardingDone() {
        prefs.edit().putBoolean(KEY_ONBOARDING_DONE, true).apply();
    }
    public boolean isOnboardingDone() {
        return prefs.getBoolean(KEY_ONBOARDING_DONE, false);
    }
    public void setDarkMode(boolean enabled) {
        prefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply();
    }
    public boolean isDarkMode() {
        return prefs.getBoolean(KEY_DARK_MODE, false);
    }
    public void setNotifDaily(boolean on) {
        prefs.edit().putBoolean(KEY_NOTIF_DAILY, on).apply();
    }
    public boolean isNotifDaily() {
        return prefs.getBoolean(KEY_NOTIF_DAILY, true);
    }
    public void setNotifStreak(boolean on) {
        prefs.edit().putBoolean(KEY_NOTIF_STREAK, on).apply();
    }
    public boolean isNotifStreak() {
        return prefs.getBoolean(KEY_NOTIF_STREAK, true);
    }
    public void setNotifBadge(boolean on) {
        prefs.edit().putBoolean(KEY_NOTIF_BADGE, on).apply();
    }
    public boolean isNotifBadge() {
        return prefs.getBoolean(KEY_NOTIF_BADGE, true);
    }
    public void setNotifFriend(boolean on) {
        prefs.edit().putBoolean(KEY_NOTIF_FRIEND, on).apply();
    }
    public boolean isNotifFriend() {
        return prefs.getBoolean(KEY_NOTIF_FRIEND, true);
    }
    public void setNotifDailyChallenge(boolean on) {
        prefs.edit().putBoolean(KEY_NOTIF_DAILY_CHALLENGE, on).apply();
    }
    public boolean isNotifDailyChallenge() {
        return prefs.getBoolean(KEY_NOTIF_DAILY_CHALLENGE, true);
    }
    public void setNotifMutual(boolean on) {
        prefs.edit().putBoolean(KEY_NOTIF_MUTUAL, on).apply();
    }
    public boolean isNotifMutual() {
        return prefs.getBoolean(KEY_NOTIF_MUTUAL, true);
    }
    public void setDailyReminderHour(int hour) {
        prefs.edit().putInt(KEY_DAILY_REMINDER_HOUR, hour).apply();
    }
    public int getDailyReminderHour() {
        return prefs.getInt(KEY_DAILY_REMINDER_HOUR, 20);
    } 
    public void saveLessonPosition(long lessonId, int slideIndex) {
        prefs.edit().putInt("lesson_pos_" + lessonId, slideIndex).apply();
    }
    public int getLessonPosition(long lessonId) {
        return prefs.getInt("lesson_pos_" + lessonId, 0);
    }
    public void clearLessonPosition(long lessonId) {
        prefs.edit().remove("lesson_pos_" + lessonId).apply();
    }
    public void setLanguage(String lang) {
        prefs.edit().putString(KEY_LANGUAGE, lang).apply();
    }
    public String getLanguage() {
        String defaultLanguage = java.util.Locale.getDefault().getLanguage().startsWith("en") ? "en" : "fr";
        return prefs.getString(KEY_LANGUAGE, defaultLanguage);
    }
    public void logout() {
        prefs.edit()
                .remove(KEY_ACCESS_TOKEN)
                .remove(KEY_REFRESH_TOKEN)
                .remove(KEY_USER_ID)
                .remove(KEY_USER_PSEUDO)
                .putBoolean(KEY_IS_LOGGED_IN, false)
                .apply();
    }
}
