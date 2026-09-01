package com.example.mafunzo.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

public class UserPreferences {

    private static final String PREFS_NAME =
            "UserPrefs";

    private static final String KEY_FIRST_NAME =
            "firstName";

    private static final String KEY_LAST_NAME =
            "lastName";

    private static final String KEY_EMAIL =
            "email";

    private static final String KEY_OBJECTIVE =
            "objective";

    private static final String KEY_INTERESTS =
            "interests";

    private static final String KEY_LEVEL =
            "level";

    private static final String KEY_PACE =
            "pace";

    private static final String KEY_PROFILE_PHOTO =
            "profilePhotoUri";

    private final SharedPreferences preferences;

    public UserPreferences(Context context) {

        preferences =
                context.getApplicationContext()
                        .getSharedPreferences(
                                PREFS_NAME,
                                Context.MODE_PRIVATE
                        );
    }

    public String getFirstName() {

        return preferences.getString(
                KEY_FIRST_NAME,
                "Utilisateur"
        );
    }

    public String getLastName() {

        return preferences.getString(
                KEY_LAST_NAME,
                ""
        );
    }

    public String getEmail() {

        return preferences.getString(
                KEY_EMAIL,
                "Aucun e-mail"
        );
    }

    public void setFirstName(
            String firstName
    ) {

        preferences.edit()
                .putString(
                        KEY_FIRST_NAME,
                        firstName
                )
                .apply();
    }

    public void setLastName(
            String lastName
    ) {

        preferences.edit()
                .putString(
                        KEY_LAST_NAME,
                        lastName
                )
                .apply();
    }

    public void setEmail(
            String email
    ) {

        preferences.edit()
                .putString(
                        KEY_EMAIL,
                        email
                )
                .apply();
    }

    public String getObjective() {

        return preferences.getString(
                KEY_OBJECTIVE,
                ""
        );
    }

    public String getInterests() {

        return preferences.getString(
                KEY_INTERESTS,
                ""
        );
    }

    public String getLevel() {

        return preferences.getString(
                KEY_LEVEL,
                ""
        );
    }

    public String getPace() {

        return preferences.getString(
                KEY_PACE,
                ""
        );
    }

    public void setObjective(
            String objective
    ) {

        preferences.edit()
                .putString(
                        KEY_OBJECTIVE,
                        objective
                )
                .apply();
    }

    public void setInterests(
            String interests
    ) {

        preferences.edit()
                .putString(
                        KEY_INTERESTS,
                        interests
                )
                .apply();
    }

    public void setLevel(
            String level
    ) {

        preferences.edit()
                .putString(
                        KEY_LEVEL,
                        level
                )
                .apply();
    }

    public void setPace(
            String pace
    ) {

        preferences.edit()
                .putString(
                        KEY_PACE,
                        pace
                )
                .apply();
    }

    public String getProfilePhotoUri() {

        return preferences.getString(
                KEY_PROFILE_PHOTO,
                ""
        );
    }

    public void setProfilePhotoUri(
            Uri uri
    ) {

        if (uri == null) {
            return;
        }

        preferences.edit()
                .putString(
                        KEY_PROFILE_PHOTO,
                        uri.toString()
                )
                .apply();
    }

    public void removeProfilePhoto() {

        preferences.edit()
                .remove(
                        KEY_PROFILE_PHOTO
                )
                .apply();
    }

    public String getFullName() {

        String firstName =
                getFirstName();

        String lastName =
                getLastName();

        return (
                firstName
                        + " "
                        + lastName
        ).trim();
    }

    public void clearUserData() {

        preferences.edit()
                .clear()
                .apply();
    }
}