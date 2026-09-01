package com.example.mafunzo.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSettings {

    private static final String PREFS_NAME =
            "AppSettings";

    private static final String KEY_NOTIFICATIONS =
            "notificationsEnabled";

    private final SharedPreferences preferences;

    public AppSettings(Context context) {

        preferences =
                context.getApplicationContext()
                        .getSharedPreferences(
                                PREFS_NAME,
                                Context.MODE_PRIVATE
                        );
    }

    public boolean areNotificationsEnabled() {

        return preferences.getBoolean(
                KEY_NOTIFICATIONS,
                true
        );
    }

    public void setNotificationsEnabled(
            boolean enabled
    ) {

        preferences.edit()
                .putBoolean(
                        KEY_NOTIFICATIONS,
                        enabled
                )
                .apply();
    }
}