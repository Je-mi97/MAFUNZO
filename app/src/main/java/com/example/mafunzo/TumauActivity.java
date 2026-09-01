package com.example.mafunzo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class TumauActivity extends AppCompatActivity {

    private ChipGroup cgObjectives, cgInterests;
    private RadioGroup rgLevel;
    private MaterialButtonToggleGroup togglePace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tumau);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scroll_tumau), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialisation des composants
        cgObjectives = findViewById(R.id.cg_objectives);
        cgInterests = findViewById(R.id.cg_interests);
        rgLevel = findViewById(R.id.rg_level);
        togglePace = findViewById(R.id.toggle_pace);
        MaterialButton btnConfirm = findViewById(R.id.btn_tumau_confirm);

        btnConfirm.setOnClickListener(v -> {
            if (validateSelections()) {
                saveUserData();
            }
        });
    }

    private boolean validateSelections() {
        if (cgObjectives.getCheckedChipId() == -1) {
            Toast.makeText(this, "Veuillez choisir un objectif", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (cgInterests.getCheckedChipIds().isEmpty()) {
            Toast.makeText(this, "Veuillez choisir au moins un intérêt", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (rgLevel.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Veuillez indiquer votre niveau", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (togglePace.getCheckedButtonId() == -1) {
            Toast.makeText(this, "Veuillez choisir un rythme", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveUserData() {
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("firstName", getIntent().getStringExtra("firstName"));
        editor.putString("lastName", getIntent().getStringExtra("lastName"));
        editor.putString("email", getIntent().getStringExtra("email"));

        int objectiveId = cgObjectives.getCheckedChipId();
        Chip chipObjective = findViewById(objectiveId);
        editor.putString("objective", chipObjective.getText().toString());

        List<Integer> interestIds = cgInterests.getCheckedChipIds();
        StringBuilder interests = new StringBuilder();
        for (Integer id : interestIds) {
            Chip chip = findViewById(id);
            interests.append(chip.getText().toString()).append(",");
        }
        editor.putString("interests", interests.toString());

        int levelId = rgLevel.getCheckedRadioButtonId();
        RadioButton rbLevel = findViewById(levelId);
        editor.putString("level", rbLevel.getText().toString());

        int paceId = togglePace.getCheckedButtonId();
        MaterialButton btnPace = findViewById(paceId);
        editor.putString("pace", btnPace.getText().toString());

        editor.apply();

        Toast.makeText(this, "Inscription terminée !", Toast.LENGTH_LONG).show();
        
        // CORRECTION A : Navigation vers LoadingActivity avant de fermer
        Intent intent = new Intent(TumauActivity.this, LoadingActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}
