package com.example.mafunzo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mafunzo.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class WauActivity extends AppCompatActivity {

    private TextInputLayout tilFirstName, tilLastName, tilEmail;
    private TextInputLayout tilPhone;
    private TextInputLayout tilPassword, tilConfirmPassword;

    private TextInputEditText etFirstName, etLastName, etEmail;
    private TextInputEditText etPhone;
    private TextInputEditText etPassword, etConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wau);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.wau_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Champs
        tilFirstName = findViewById(R.id.til_first_name);
        tilLastName = findViewById(R.id.til_last_name);
        tilEmail = findViewById(R.id.til_wau_email);
        tilPhone = findViewById(R.id.til_wau_phone);
        tilPassword = findViewById(R.id.til_wau_password);
        tilConfirmPassword = findViewById(R.id.til_wau_confirm_password);

        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etEmail = findViewById(R.id.et_wau_email);
        etPhone = findViewById(R.id.et_wau_phone);
        etPassword = findViewById(R.id.et_wau_password);
        etConfirmPassword = findViewById(R.id.et_wau_confirm_password);

        MaterialButton btnNext =
                findViewById(R.id.btn_wau_next);

        btnNext.setOnClickListener(v -> {

            if (validateForm()) {

                Intent intent =
                        new Intent(
                                WauActivity.this,
                                TumauActivity.class
                        );

                // Prénom
                intent.putExtra(
                        "firstName",
                        etFirstName.getText().toString().trim()
                );

                // Nom
                intent.putExtra(
                        "lastName",
                        etLastName.getText().toString().trim()
                );

                // Email
                intent.putExtra(
                        "email",
                        etEmail.getText().toString().trim()
                );

                // Téléphone
                intent.putExtra(
                        "phone",
                        etPhone.getText().toString().trim()
                );

                // Mot de passe
                intent.putExtra(
                        "password",
                        etPassword.getText().toString()
                );

                startActivity(intent);
            }
        });
    }

    private boolean validateForm() {

        boolean isValid = true;

        String firstName =
                etFirstName.getText().toString().trim();

        String lastName =
                etLastName.getText().toString().trim();

        String email =
                etEmail.getText().toString().trim();

        String phone =
                etPhone.getText().toString().trim();

        String password =
                etPassword.getText().toString();

        String confirmPassword =
                etConfirmPassword.getText().toString();

        // Prénom
        if (TextUtils.isEmpty(firstName)) {

            tilFirstName.setError(
                    "Le prénom est requis"
            );

            isValid = false;

        } else {

            tilFirstName.setError(null);
        }

        // Nom
        if (TextUtils.isEmpty(lastName)) {

            tilLastName.setError(
                    "Le nom est requis"
            );

            isValid = false;

        } else {

            tilLastName.setError(null);
        }

        // Email
        if (TextUtils.isEmpty(email)) {

            tilEmail.setError(
                    "L'e-mail est requis"
            );

            isValid = false;

        } else if (!Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()) {

            tilEmail.setError(
                    "Format d'e-mail invalide"
            );

            isValid = false;

        } else {

            tilEmail.setError(null);
        }

        // Téléphone
        if (TextUtils.isEmpty(phone)) {

            tilPhone.setError(
                    "Le numéro de téléphone est requis"
            );

            isValid = false;

        } else {

            tilPhone.setError(null);
        }

        // Mot de passe
        if (TextUtils.isEmpty(password)) {

            tilPassword.setError(
                    "Le mot de passe est requis"
            );

            isValid = false;

        } else if (password.length() < 6) {

            tilPassword.setError(
                    "Le mot de passe doit contenir au moins 6 caractères"
            );

            isValid = false;

        } else {

            tilPassword.setError(null);
        }

        // Confirmation
        if (TextUtils.isEmpty(confirmPassword)) {

            tilConfirmPassword.setError(
                    "Confirmez votre mot de passe"
            );

            isValid = false;

        } else if (!password.equals(confirmPassword)) {

            tilConfirmPassword.setError(
                    "Les mots de passe ne correspondent pas"
            );

            isValid = false;

        } else {

            tilConfirmPassword.setError(null);
        }

        return isValid;
    }
}