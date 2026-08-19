package com.example.mafunzo.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mafunzo.R;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences settings;

    private Switch switchNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_settings
        );

        settings =
                getSharedPreferences(
                        "AppSettings",
                        Context.MODE_PRIVATE
                );

        ImageButton btnBack =
                findViewById(
                        R.id.btn_back_settings
                );

        switchNotifications =
                findViewById(
                        R.id.switch_notifications
                );

        btnBack.setOnClickListener(
                v -> finish()
        );

        boolean enabled =
                settings.getBoolean(
                        "notificationsEnabled",
                        true
                );

        switchNotifications.setChecked(
                enabled
        );

        switchNotifications.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    settings.edit()
                            .putBoolean(
                                    "notificationsEnabled",
                                    isChecked
                            )
                            .apply();

                    Toast.makeText(
                            this,
                            isChecked
                                    ? "Notifications activées"
                                    : "Notifications désactivées",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );
    }
}