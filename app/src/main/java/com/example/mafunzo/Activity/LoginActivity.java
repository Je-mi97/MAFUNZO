package com.example.mafunzo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mafunzo.Model.AuthResponse;
import com.example.mafunzo.Model.LoginRequest;
import com.example.mafunzo.Model.UserResponse;
import com.example.mafunzo.R;
import com.example.mafunzo.Utils.ApiService;
import com.example.mafunzo.Utils.RetrofitClient;
import com.example.mafunzo.Utils.SessionManager;
import com.example.mafunzo.Utils.UserPreferences;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etUsernameEmail;
    private TextInputEditText etPassword;

    private SessionManager sessionManager;
    private UserPreferences userPreferences;

    private MaterialButton btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        userPreferences = new UserPreferences(this);

        etUsernameEmail = findViewById(
                R.id.et_username_email
        );

        etPassword = findViewById(
                R.id.et_password
        );

        btnLogin = findViewById(
                R.id.btn_login
        );

        btnLogin.setOnClickListener(
                v -> login()
        );
    }

    private void login() {

        String email = etUsernameEmail
                .getText()
                .toString()
                .trim();

        String password = etPassword
                .getText()
                .toString();

        if (TextUtils.isEmpty(email)) {

            etUsernameEmail.setError(
                    "Entrez votre e-mail."
            );

            etUsernameEmail.requestFocus();

            return;
        }

        if (!Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()) {

            etUsernameEmail.setError(
                    "E-mail invalide."
            );

            etUsernameEmail.requestFocus();

            return;
        }

        if (TextUtils.isEmpty(password)) {

            etPassword.setError(
                    "Entrez votre mot de passe."
            );

            etPassword.requestFocus();

            return;
        }

        btnLogin.setEnabled(false);
        btnLogin.setText("Connexion...");

        LoginRequest request =
                new LoginRequest(
                        email,
                        password
                );

        ApiService apiService =
                RetrofitClient
                        .getClient()
                        .create(ApiService.class);

        Call<AuthResponse> call =
                apiService.login(request);

        call.enqueue(
                new Callback<AuthResponse>() {

                    @Override
                    public void onResponse(
                            Call<AuthResponse> call,
                            Response<AuthResponse> response
                    ) {

                        btnLogin.setEnabled(true);
                        btnLogin.setText(
                                R.string.btn_login
                        );

                        if (response.isSuccessful()
                                && response.body() != null) {

                            AuthResponse authResponse =
                                    response.body();

                            UserResponse user =
                                    authResponse.getUser();

                            if (user == null) {

                                Toast.makeText(
                                        LoginActivity.this,
                                        "Réponse utilisateur invalide.",
                                        Toast.LENGTH_LONG
                                ).show();

                                return;
                            }

                            /*
                             * Sauvegarde des informations
                             * retournées par le backend.
                             */
                            userPreferences.setFirstName(
                                    user.getFirstName()
                            );

                            userPreferences.setLastName(
                                    user.getLastName()
                            );

                            userPreferences.setEmail(
                                    user.getEmail()
                            );

                            /*
                             * Sauvegarde du téléphone
                             * si nécessaire dans UserPreferences.
                             */
                            if (user.getPhone() != null) {
                                // Le téléphone sera disponible
                                // côté backend même s'il n'est
                                // pas encore utilisé dans le profil.
                            }

                            /*
                             * Sauvegarde du token JWT.
                             * Pour l'instant, SessionManager
                             * gère l'état de connexion.
                             */
                            sessionManager.createSession();

                            Toast.makeText(
                                    LoginActivity.this,
                                    "Connexion réussie.",
                                    Toast.LENGTH_SHORT
                            ).show();

                            openHome();

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

                            String message =
                                    "Erreur de connexion "
                                            + response.code();

                            if (!TextUtils.isEmpty(errorBody)) {

                                message +=
                                        " : " + errorBody;
                            }

                            Toast.makeText(
                                    LoginActivity.this,
                                    message,
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<AuthResponse> call,
                            Throwable t
                    ) {

                        btnLogin.setEnabled(true);
                        btnLogin.setText(
                                R.string.btn_login
                        );

                        Toast.makeText(
                                LoginActivity.this,
                                "Impossible de contacter le serveur : "
                                        + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }

    private void openHome() {

        Intent intent =
                new Intent(
                        LoginActivity.this,
                        HomeActivity.class
                );

        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
        );

        startActivity(intent);

        finish();
    }
}