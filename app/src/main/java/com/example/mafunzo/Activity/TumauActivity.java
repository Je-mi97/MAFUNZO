package com.example.mafunzo.Activity;

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

import com.example.mafunzo.Model.RegisterRequest;
import com.example.mafunzo.Model.UserResponse;
import com.example.mafunzo.R;
import com.example.mafunzo.Utils.ApiService;
import com.example.mafunzo.Utils.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TumauActivity extends AppCompatActivity {

    private ChipGroup cgObjectives;
    private ChipGroup cgInterests;
    private RadioGroup rgLevel;
    private MaterialButtonToggleGroup togglePace;

    private MaterialButton btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tumau);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.scroll_tumau),
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
                            );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );

        cgObjectives =
                findViewById(R.id.cg_objectives);

        cgInterests =
                findViewById(R.id.cg_interests);

        rgLevel =
                findViewById(R.id.rg_level);

        togglePace =
                findViewById(R.id.toggle_pace);

        btnConfirm =
                findViewById(R.id.btn_tumau_confirm);

        btnConfirm.setOnClickListener(v -> {

            if (validateSelections()) {
                registerUser();
            }
        });
    }

    private boolean validateSelections() {

        if (cgObjectives.getCheckedChipId() == -1) {

            Toast.makeText(
                    this,
                    "Veuillez choisir un objectif",
                    Toast.LENGTH_SHORT
            ).show();

            return false;
        }

        if (cgInterests.getCheckedChipIds().isEmpty()) {

            Toast.makeText(
                    this,
                    "Veuillez choisir au moins un intérêt",
                    Toast.LENGTH_SHORT
            ).show();

            return false;
        }

        if (rgLevel.getCheckedRadioButtonId() == -1) {

            Toast.makeText(
                    this,
                    "Veuillez indiquer votre niveau",
                    Toast.LENGTH_SHORT
            ).show();

            return false;
        }

        if (togglePace.getCheckedButtonId() == -1) {

            Toast.makeText(
                    this,
                    "Veuillez choisir un rythme",
                    Toast.LENGTH_SHORT
            ).show();

            return false;
        }

        return true;
    }

    private void registerUser() {

        String firstName =
                getIntent().getStringExtra("firstName");

        String lastName =
                getIntent().getStringExtra("lastName");

        String email =
                getIntent().getStringExtra("email");

        String phone =
                getIntent().getStringExtra("phone");

        String password =
                getIntent().getStringExtra("password");

        if (firstName == null ||
                lastName == null ||
                email == null ||
                phone == null ||
                password == null) {

            Toast.makeText(
                    this,
                    "Données d'inscription manquantes.",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        // OBJECTIF
        int objectiveId =
                cgObjectives.getCheckedChipId();

        Chip chipObjective =
                findViewById(objectiveId);

        String objective =
                chipObjective.getText().toString();

        // INTÉRÊTS
        List<Integer> interestIds =
                cgInterests.getCheckedChipIds();

        StringBuilder interests =
                new StringBuilder();

        for (Integer id : interestIds) {

            Chip chip =
                    findViewById(id);

            if (interests.length() > 0) {
                interests.append(", ");
            }

            interests.append(
                    chip.getText().toString()
            );
        }

        // NIVEAU
        int levelId =
                rgLevel.getCheckedRadioButtonId();

        RadioButton rbLevel =
                findViewById(levelId);

        String level =
                rbLevel.getText().toString();

        // RYTHME
        int paceId =
                togglePace.getCheckedButtonId();

        MaterialButton btnPace =
                findViewById(paceId);

        String pace =
                btnPace.getText().toString();

        // REQUÊTE COMPLÈTE
        RegisterRequest request =
                new RegisterRequest(
                        firstName,
                        lastName,
                        email,
                        phone,
                        password,
                        objective,
                        interests.toString(),
                        level,
                        pace
                );

        btnConfirm.setEnabled(false);
        btnConfirm.setText("Création du compte...");

        ApiService apiService =
                RetrofitClient
                        .getClient()
                        .create(ApiService.class);

        Call<UserResponse> call =
                apiService.register(request);

        call.enqueue(new Callback<UserResponse>() {

            @Override
            public void onResponse(
                    Call<UserResponse> call,
                    Response<UserResponse> response
            ) {

                btnConfirm.setEnabled(true);

                btnConfirm.setText(
                        R.string.btn_confirm
                );

                if (response.isSuccessful()
                        && response.body() != null) {

                    saveUserData(
                            objective,
                            interests.toString(),
                            level,
                            pace
                    );

                    Toast.makeText(
                            TumauActivity.this,
                            "Inscription terminée !",
                            Toast.LENGTH_LONG
                    ).show();

                    Intent intent =
                            new Intent(
                                    TumauActivity.this,
                                    HomeActivity.class
                            );

                    startActivity(intent);

                    finishAffinity();

                } else {

                    String errorBody = "";

                    try {

                        if (response.errorBody() != null) {

                            errorBody =
                                    response.errorBody()
                                            .string();
                        }

                    } catch (Exception e) {

                        errorBody =
                                e.getMessage();
                    }

                    Toast.makeText(
                            TumauActivity.this,
                            "Erreur "
                                    + response.code()
                                    + " : "
                                    + errorBody,
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<UserResponse> call,
                    Throwable t
            ) {

                btnConfirm.setEnabled(true);

                btnConfirm.setText(
                        R.string.btn_confirm
                );

                Toast.makeText(
                        TumauActivity.this,
                        "Impossible de contacter le serveur : "
                                + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void saveUserData(
            String objective,
            String interests,
            String level,
            String pace
    ) {

        SharedPreferences sharedPref =
                getSharedPreferences(
                        "UserPrefs",
                        Context.MODE_PRIVATE
                );

        SharedPreferences.Editor editor =
                sharedPref.edit();

        editor.putString(
                "firstName",
                getIntent().getStringExtra("firstName")
        );

        editor.putString(
                "lastName",
                getIntent().getStringExtra("lastName")
        );

        editor.putString(
                "email",
                getIntent().getStringExtra("email")
        );

        editor.putString(
                "phone",
                getIntent().getStringExtra("phone")
        );

        editor.putString(
                "objective",
                objective
        );

        editor.putString(
                "interests",
                interests
        );

        editor.putString(
                "level",
                level
        );

        editor.putString(
                "pace",
                pace
        );

        editor.apply();
    }
}