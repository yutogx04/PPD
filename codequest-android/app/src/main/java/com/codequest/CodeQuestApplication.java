package com.codequest;

import android.app.Application;
import androidx.appcompat.app.AppCompatDelegate;
import com.codequest.network.AuthInterceptor;
import com.codequest.network.RetrofitClient;
import com.codequest.network.TokenAuthenticator;
import com.codequest.util.SharedPrefManager;

public class CodeQuestApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitClient.init(new AuthInterceptor(this), new TokenAuthenticator(this));

        SharedPrefManager prefs = SharedPrefManager.getInstance(this);
        if (prefs.isDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
