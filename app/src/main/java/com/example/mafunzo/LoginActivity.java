package com.example.mafunzo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilUsername, tilPassword;
    private TextInputEditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tilUsername = findViewById(R.id.til_username);
        tilPassword = findViewById(R.id.til_password);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);

        MaterialButton btnLogin = findViewById(R.id.btn_login);
        MaterialButton btnForgot = findViewById(R.id.btn_forgot_password);

        btnLogin.setOnClickListener(v -> {
            if (validateLogin()) {
                // Logique de connexion réussie
                Toast.makeText(this, "Connexion réussie !", Toast.LENGTH_SHORT).show();
                
                // Redirection vers le Dashboard (HOME)
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finishAffinity(); // On ferme l'écran de login pour ne pas y revenir
            }
        });

        btnForgot.setOnClickListener(v -> {
            Toast.makeText(this, "Redirection vers récupération...", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean validateLogin() {
        boolean isValid = true;
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            tilUsername.setError("L'identifiant est requis");
            isValid = false;
        } else {
            tilUsername.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("Le mot de passe est requis");
            isValid = false;
        } else if (password.length() < 6) {
            tilPassword.setError("Le mot de passe doit faire au moins 6 caractères");
            isValid = false;
        } else {
            tilPassword.setError(null);
        }

        return isValid;
    }
}
