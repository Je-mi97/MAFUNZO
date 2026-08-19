package com.example.mafunzo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.os.LocaleListCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mafunzo.R;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialButton btnYes = findViewById(R.id.btn_yes);
        MaterialButton btnNo = findViewById(R.id.btn_no);
        TextView tvLangEn = findViewById(R.id.tv_lang_en);
        TextView tvLangFr = findViewById(R.id.tv_lang_fr);

        btnYes.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WauActivity.class);
            startActivity(intent);
        });

        btnNo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // 4. Logique de Changement de Langue
        tvLangEn.setOnClickListener(v -> {
            setAppLocale("en");
            Toast.makeText(this, "Language set to English", Toast.LENGTH_SHORT).show();
        });

        tvLangFr.setOnClickListener(v -> {
            setAppLocale("fr");
            Toast.makeText(this, "Langue réglée sur Français", Toast.LENGTH_SHORT).show();
        });
    }

    private void setAppLocale(String languageCode) {
        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(languageCode);
        AppCompatDelegate.setApplicationLocales(appLocale);
    }
}
