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
        // Activation du mode Edge-to-Edge pour une immersion totale
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Correction du chevauchement avec la barre d'état (heure, batterie)
        // On applique les marges système dynamiquement au conteneur principal
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // On ajoute les systemBars aux paddings existants (24dp convertis ici en pixels)
            int density = (int) getResources().getDisplayMetrics().density;
            int padding = 24 * density;
            
            v.setPadding(
                systemBars.left + padding,
                systemBars.top + padding,
                systemBars.right + padding,
                systemBars.bottom + padding
            );
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
