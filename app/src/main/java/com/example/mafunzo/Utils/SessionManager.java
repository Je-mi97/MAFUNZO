package com.example.mafunzo.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREFS_NAME =
            "SessionPrefs";

    private static final String KEY_LOGGED_IN =
            "isLoggedIn";

    private final SharedPreferences preferences;

    public SessionManager(Context context) {

        preferences =
                context.getApplicationContext()
                        .getSharedPreferences(
                                PREFS_NAME,
                                Context.MODE_PRIVATE
                        );
    }

    public void createSession() {

        preferences.edit()
                .putBoolean(
                        KEY_LOGGED_IN,
                        true
                )
                .apply();
    }

    public boolean isLoggedIn() {

        return preferences.getBoolean(
                KEY_LOGGED_IN,
                false
        );
    }

    public void clearSession() {

        preferences.edit()
                .putBoolean(
                        KEY_LOGGED_IN,
                        false
                )
                .apply();
    }
}