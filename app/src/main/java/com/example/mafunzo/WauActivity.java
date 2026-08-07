package com.example.mafunzo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class WauActivity extends AppCompatActivity {

    private TextInputLayout tilFirstName, tilLastName, tilEmail;
    private TextInputEditText etFirstName, etLastName, etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_wau);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialisation des vues
        tilFirstName = findViewById(R.id.til_first_name);
        tilLastName = findViewById(R.id.til_last_name);
        tilEmail = findViewById(R.id.til_wau_email);
        
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etEmail = findViewById(R.id.et_wau_email);

        MaterialButton btnNext = findViewById(R.id.btn_wau_next);
        btnNext.setOnClickListener(v -> {
            if (validateForm()) {
                Intent intent = new Intent(WauActivity.this, TumauActivity.class);
                // On passe les données à l'activité suivante
                intent.putExtra("firstName", etFirstName.getText().toString().trim());
                intent.putExtra("lastName", etLastName.getText().toString().trim());
                intent.putExtra("email", etEmail.getText().toString().trim());
                startActivity(intent);
            }
        });
    }

    // 3. Validation des Formulaires (WAU)
    private boolean validateForm() {
        boolean isValid = true;

        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (TextUtils.isEmpty(firstName)) {
            tilFirstName.setError("Le prénom est requis");
            isValid = false;
        } else {
            tilFirstName.setError(null);
        }

        if (TextUtils.isEmpty(lastName)) {
            tilLastName.setError("Le nom est requis");
            isValid = false;
        } else {
            tilLastName.setError(null);
        }

        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("L'e-mail est requis");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Format d'e-mail invalide");
            isValid = false;
        } else {
            tilEmail.setError(null);
        }

        return isValid;
    }
}
