package com.example.mafunzo;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SeeAllActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all);

        Toolbar toolbar = findViewById(R.id.toolbar_see_all);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String category = getIntent().getStringExtra("CATEGORY");
        RecyclerView recyclerView = findViewById(R.id.rv_see_all);

        if (category != null) {
            updateUI(category, recyclerView);
        }
    }

    private void updateUI(String category, RecyclerView recyclerView) {
        switch (category) {
            case "subjects":
                setTitle(getString(R.string.section_subjects));
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                // Charger tous les sujets verticalement
                break;
            case "recommendations":
                setTitle(getString(R.string.section_recommendations));
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                // Charger toutes les recommandations
                break;
            case "careers":
                setTitle(getString(R.string.section_careers));
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                // Charger toutes les carrières
                break;
            case "certifications":
                setTitle(getString(R.string.section_certifications));
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                // Charger toutes les certifications
                break;
            case "all_courses":
                setTitle(getString(R.string.nav_courses));
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
