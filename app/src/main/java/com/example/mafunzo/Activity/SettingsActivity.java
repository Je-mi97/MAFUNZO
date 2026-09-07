package com.example.mafunzo.Activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mafunzo.R;
import com.example.mafunzo.Utils.AppSettings;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchNotifications;
    private AppSettings appSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        appSettings = new AppSettings(this);
        ImageButton btnBack = findViewById(R.id.btn_back_settings);
        switchNotifications = findViewById(R.id.switch_notifications);

        btnBack.setOnClickListener(v -> finish());

        switchNotifications.setChecked(appSettings.areNotificationsEnabled());
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            appSettings.setNotificationsEnabled(isChecked);
            Toast.makeText(this, isChecked ? "Notifications activées" : "Notifications désactivées", Toast.LENGTH_SHORT).show();
        });
    }
}