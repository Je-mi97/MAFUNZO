package com.example.mafunzo.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mafunzo.Activity.EditProfileActivity;
import com.example.mafunzo.Activity.HistoryActivity;
import com.example.mafunzo.Activity.NotificationsActivity;
import com.example.mafunzo.Activity.PaymentActivity;
import com.example.mafunzo.Activity.ProgressActivity;
import com.example.mafunzo.Activity.SettingsActivity;
import com.example.mafunzo.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

public class ProfileFragment extends Fragment {

    private TextView tvName;
    private TextView tvEmail;
    private ShapeableImageView ivProfile;

    private SharedPreferences userPrefs;

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_profile,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {

        super.onViewCreated(
                view,
                savedInstanceState
        );

        tvName = view.findViewById(
                R.id.tv_profile_name
        );

        tvEmail = view.findViewById(
                R.id.tv_profile_email
        );

        ivProfile = view.findViewById(
                R.id.iv_profile
        );

        userPrefs =
                requireContext().getSharedPreferences(
                        "UserPrefs",
                        Context.MODE_PRIVATE
                );

        MaterialButton btnEdit =
                view.findViewById(
                        R.id.btn_edit_profile
                );

        btnEdit.setOnClickListener(
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

        if (userPrefs != null) {
            loadUserData();
        }
    }

    private void loadUserData() {

        String firstName =
                userPrefs.getString(
                        "firstName",
                        "Utilisateur"
                );

        String lastName =
                userPrefs.getString(
                        "lastName",
                        ""
                );

        String email =
                userPrefs.getString(
                        "email",
                        "Aucun e-mail"
                );

        tvName.setText(
                (firstName + " " + lastName).trim()
        );

        tvEmail.setText(email);

        String photoUri =
                userPrefs.getString(
                        "profilePhotoUri",
                        ""
                );

        if (photoUri.isEmpty()) {

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

        view.findViewById(
                R.id.item_progress
        ).setOnClickListener(v ->
                startActivity(
                        new Intent(
                                requireContext(),
                                ProgressActivity.class
                        )
                )
        );

        view.findViewById(
                R.id.item_history
        ).setOnClickListener(v ->
                startActivity(
                        new Intent(
                                requireContext(),
                                HistoryActivity.class
                        )
                )
        );

        view.findViewById(
                R.id.item_notifications
        ).setOnClickListener(v ->
                startActivity(
                        new Intent(
                                requireContext(),
                                NotificationsActivity.class
                        )
                )
        );

        view.findViewById(
                R.id.item_payment
        ).setOnClickListener(v ->
                startActivity(
                        new Intent(
                                requireContext(),
                                PaymentActivity.class
                        )
                )
        );

        view.findViewById(
                R.id.item_contact
        ).setOnClickListener(v -> {

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

                android.widget.Toast.makeText(
                        requireContext(),
                        "Aucune application e-mail disponible.",
                        android.widget.Toast.LENGTH_LONG
                ).show();
            }
        });

        view.findViewById(
                R.id.item_settings
        ).setOnClickListener(v ->
                startActivity(
                        new Intent(
                                requireContext(),
                                SettingsActivity.class
                        )
                )
        );

        MaterialButton btnLogout =
                view.findViewById(
                        R.id.btn_logout
                );

        btnLogout.setOnClickListener(
                v -> logout()
        );
    }

    private void logout() {

        userPrefs
                .edit()
                .clear()
                .apply();

        requireActivity().finish();
    }
}