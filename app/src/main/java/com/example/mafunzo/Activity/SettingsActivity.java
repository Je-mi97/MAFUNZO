package com.example.mafunzo.Activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mafunzo.R;
import com.example.mafunzo.Utils.AppSettings;

public class SettingsActivity
        extends AppCompatActivity {

    private Switch switchNotifications;

    private AppSettings appSettings;

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_settings
        );

        appSettings =
                new AppSettings(this);

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

        switchNotifications.setChecked(
                appSettings
                        .areNotificationsEnabled()
        );

        switchNotifications.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    appSettings
                            .setNotificationsEnabled(
                                    isChecked
                            );

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