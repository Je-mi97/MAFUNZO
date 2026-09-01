package com.example.mafunzo.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mafunzo.Activity.EditProfileActivity;
import com.example.mafunzo.Activity.HistoryActivity;
import com.example.mafunzo.Activity.LoginActivity;
import com.example.mafunzo.Activity.NotificationsActivity;
import com.example.mafunzo.Activity.PaymentHistoryActivity;
import com.example.mafunzo.Activity.ProgressActivity;
import com.example.mafunzo.Activity.SettingsActivity;
import com.example.mafunzo.R;
import com.example.mafunzo.Utils.SessionManager;
import com.example.mafunzo.Utils.UserPreferences;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

public class ProfileFragment extends Fragment {

    private TextView tvName;
    private TextView tvEmail;

    private ShapeableImageView ivProfile;

    private UserPreferences userPreferences;
    private SessionManager sessionManager;

    public ProfileFragment() {
        // Constructeur vide
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        return inflater.inflate(
                R.layout.fragment_profile,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {

        super.onViewCreated(
                view,
                savedInstanceState
        );

        tvName =
                view.findViewById(
                        R.id.tv_profile_name
                );

        tvEmail =
                view.findViewById(
                        R.id.tv_profile_email
                );

        ivProfile =
                view.findViewById(
                        R.id.iv_profile
                );

        userPreferences =
                new UserPreferences(
                        requireContext()
                );

        sessionManager =
                new SessionManager(
                        requireContext()
                );

        MaterialButton btnEditProfile =
                view.findViewById(
                        R.id.btn_edit_profile
                );

        btnEditProfile.setOnClickListener(
                v -> startActivity(
                        new Intent(
                                requireContext(),
                                EditProfileActivity.class
                        )
                )
        );

        setupClickListeners(view);

        loadUserData();
    }

    @Override
    public void onResume() {

        super.onResume();

        if (userPreferences != null) {
            loadUserData();
        }
    }

    private void loadUserData() {

        tvName.setText(
                userPreferences.getFullName()
        );

        tvEmail.setText(
                userPreferences.getEmail()
        );

        String photoUri =
                userPreferences
                        .getProfilePhotoUri();

        if (photoUri == null
                || photoUri.isEmpty()) {

            ivProfile.setImageResource(
                    R.drawable.ic_profile_placeholder
            );

            return;
        }

        try {

            ivProfile.setImageURI(
                    Uri.parse(photoUri)
            );

        } catch (Exception e) {

            ivProfile.setImageResource(
                    R.drawable.ic_profile_placeholder
            );
        }
    }

    private void setupClickListeners(
            View view
    ) {

        // PROGRESSION

        view.findViewById(
                R.id.item_progress
        ).setOnClickListener(
                v -> startActivity(
                        new Intent(
                                requireContext(),
                                ProgressActivity.class
                        )
                )
        );

        // HISTORIQUE DES FORMATIONS

        view.findViewById(
                R.id.item_history
        ).setOnClickListener(
                v -> startActivity(
                        new Intent(
                                requireContext(),
                                HistoryActivity.class
                        )
                )
        );

        // NOTIFICATIONS

        view.findViewById(
                R.id.item_notifications
        ).setOnClickListener(
                v -> startActivity(
                        new Intent(
                                requireContext(),
                                NotificationsActivity.class
                        )
                )
        );

        // CONTACT

        view.findViewById(
                R.id.item_contact
        ).setOnClickListener(
                v -> openContact()
        );

        // MES PAIEMENTS

        view.findViewById(
                R.id.item_payment
        ).setOnClickListener(
                v -> openPaymentHistory()
        );

        // PARAMÈTRES

        view.findViewById(
                R.id.item_settings
        ).setOnClickListener(
                v -> startActivity(
                        new Intent(
                                requireContext(),
                                SettingsActivity.class
                        )
                )
        );

        // DÉCONNEXION

        MaterialButton btnLogout =
                view.findViewById(
                        R.id.btn_logout
                );

        btnLogout.setOnClickListener(
                v -> logout()
        );
    }

    private void openPaymentHistory() {

        Intent intent =
                new Intent(
                        requireContext(),
                        PaymentHistoryActivity.class
                );

        startActivity(intent);
    }

    private void openContact() {

        Intent emailIntent =
                new Intent(
                        Intent.ACTION_SENDTO
                );

        emailIntent.setData(
                Uri.parse(
                        "mailto:mafunzo@example.com"
                )
        );

        emailIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                "Contact MAFUNZO"
        );

        try {

            startActivity(emailIntent);

        } catch (Exception e) {

            Toast.makeText(
                    requireContext(),
                    "Aucune application e-mail disponible.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private void logout() {

        sessionManager.clearSession();

        Toast.makeText(
                requireContext(),
                "Déconnexion réussie",
                Toast.LENGTH_SHORT
        ).show();

        Intent intent =
                new Intent(
                        requireContext(),
                        LoginActivity.class
                );

        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);
    }
}