package com.codequest.ui.base;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import com.codequest.util.LocaleHelper;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase)));
    }
}
