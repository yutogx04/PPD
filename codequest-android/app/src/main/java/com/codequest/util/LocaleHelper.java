package com.codequest.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import java.util.Locale;

public class LocaleHelper {

    public static Context setLocale(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = new Configuration(resources.getConfiguration());
        config.setLocale(locale);

        return context.createConfigurationContext(config);
    }

    public static String getLanguage(Context context) {
        return SharedPrefManager.getInstance(context).getLanguage();
    }
}
