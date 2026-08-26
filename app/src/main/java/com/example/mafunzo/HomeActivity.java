package com.example.mafunzo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvSubjects, rvCareers;
    private final Handler autoScrollHandler = new Handler();
    private int scrollPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // Initialisation des listes
        rvSubjects = findViewById(R.id.rv_subjects);
        rvCareers = findViewById(R.id.rv_careers);
        
        setupSeeAllButtons();
        startAutoScroll();
        
        // Navigation basse
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) return true;
            if (itemId == R.id.nav_courses) {
                openSeeAll("all_courses");
                return true;
            }
            if (itemId == R.id.nav_profile) {
                Toast.makeText(this, "Profil", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }

    private void setupSeeAllButtons() {
        findViewById(R.id.btn_see_all_subjects).setOnClickListener(v -> openSeeAll("subjects"));
        findViewById(R.id.btn_see_all_recommendations).setOnClickListener(v -> openSeeAll("recommendations"));
        findViewById(R.id.btn_see_all_careers).setOnClickListener(v -> openSeeAll("careers"));
        findViewById(R.id.btn_see_all_certifications).setOnClickListener(v -> openSeeAll("certifications"));
    }

    private void openSeeAll(String category) {
        Intent intent = new Intent(this, SeeAllActivity.class);
        intent.putExtra("CATEGORY", category);
        startActivity(intent);
    }

    // Logique de défilement automatique pour Sujets et Carrières
    private void startAutoScroll() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (scrollPosition < 10) { // On fait défiler une partie (ex: 10 premiers)
                    scrollPosition++;
                } else {
                    scrollPosition = 0;
                }
                rvSubjects.smoothScrollToPosition(scrollPosition);
                rvCareers.smoothScrollToPosition(scrollPosition);
                autoScrollHandler.postDelayed(this, 3000); // Toutes les 3 secondes
            }
        };
        autoScrollHandler.postDelayed(runnable, 3000);
    }
}
