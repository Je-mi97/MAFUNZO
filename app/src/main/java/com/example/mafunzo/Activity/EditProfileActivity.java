package com.example.mafunzo.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mafunzo.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class EditProfileActivity extends AppCompatActivity {

    private TextInputLayout tilFirstName;
    private TextInputLayout tilLastName;
    private TextInputLayout tilEmail;

    private TextInputEditText etFirstName;
    private TextInputEditText etLastName;
    private TextInputEditText etEmail;

    private ChipGroup cgObjectives;
    private ChipGroup cgInterests;

    private RadioGroup rgLevel;

    private MaterialButtonToggleGroup togglePace;

    private ShapeableImageView ivProfile;
    private MaterialButton btnChangePhoto;
    private MaterialButton btnSave;

    private SharedPreferences userPrefs;

    private Uri selectedPhotoUri;

    private final ActivityResultLauncher<String[]> imagePicker =
            registerForActivityResult(
                    new ActivityResultContracts.OpenDocument(),
                    uri -> {

                        if (uri == null) {
                            return;
                        }

                        selectedPhotoUri = uri;

                        try {
                            getContentResolver()
                                    .takePersistableUriPermission(
                                            uri,
                                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    );
                        } catch (SecurityException ignored) {
                            // Certains fournisseurs ne permettent pas
                            // la persistance des permissions.
                        }

                        ivProfile.setImageURI(
                                selectedPhotoUri
                        );
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_edit_profile
        );

        userPrefs =
                getSharedPreferences(
                        "UserPrefs",
                        Context.MODE_PRIVATE
                );

        ImageButton btnBack =
                findViewById(
                        R.id.btn_back_edit_profile
                );

        ivProfile =
                findViewById(
                        R.id.iv_edit_profile
                );

        btnChangePhoto =
                findViewById(
                        R.id.btn_change_photo
                );

        tilFirstName =
                findViewById(
                        R.id.til_edit_first_name
                );

        tilLastName =
                findViewById(
                        R.id.til_edit_last_name
                );

        tilEmail =
                findViewById(
                        R.id.til_edit_email
                );

        etFirstName =
                findViewById(
                        R.id.et_edit_first_name
                );

        etLastName =
                findViewById(
                        R.id.et_edit_last_name
                );

        etEmail =
                findViewById(
                        R.id.et_edit_email
                );

        cgObjectives =
                findViewById(
                        R.id.cg_edit_objectives
                );

        cgInterests =
                findViewById(
                        R.id.cg_edit_interests
                );

        rgLevel =
                findViewById(
                        R.id.rg_edit_level
                );

        togglePace =
                findViewById(
                        R.id.toggle_edit_pace
                );

        btnSave =
                findViewById(
                        R.id.btn_save_profile
                );

        btnBack.setOnClickListener(
                v -> finish()
        );

        btnChangePhoto.setOnClickListener(
                v -> imagePicker.launch(
                        new String[]{"image/*"}
                )
        );

        loadCurrentData();

        btnSave.setOnClickListener(
                v -> saveProfile()
        );
    }

    private void loadCurrentData() {

        etFirstName.setText(
                userPrefs.getString(
                        "firstName",
                        ""
                )
        );

        etLastName.setText(
                userPrefs.getString(
                        "lastName",
                        ""
                )
        );

        etEmail.setText(
                userPrefs.getString(
                        "email",
                        ""
                )
        );

        loadSavedPhoto();

        selectObjective();
        selectInterests();
        selectLevel();
        selectPace();
    }

    private void loadSavedPhoto() {

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

            Uri uri =
                    Uri.parse(photoUri);

            getContentResolver()
                    .takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    );

            selectedPhotoUri = uri;

            ivProfile.setImageURI(uri);

        } catch (Exception e) {

            ivProfile.setImageResource(
                    R.drawable.ic_profile_placeholder
            );
        }
    }

    private void selectObjective() {

        String objective =
                userPrefs.getString(
                        "objective",
                        ""
                );

        for (int i = 0;
             i < cgObjectives.getChildCount();
             i++) {

            Chip chip =
                    (Chip) cgObjectives.getChildAt(i);

            if (chip.getText()
                    .toString()
                    .equals(objective)) {

                chip.setChecked(true);
                break;
            }
        }
    }

    private void selectInterests() {

        String interests =
                userPrefs.getString(
                        "interests",
                        ""
                );

        String[] selected =
                interests.split(",");

        for (int i = 0;
             i < cgInterests.getChildCount();
             i++) {

            Chip chip =
                    (Chip) cgInterests.getChildAt(i);

            String chipText =
                    chip.getText()
                            .toString();

            for (String interest :
                    selected) {

                if (chipText.equals(
                        interest.trim()
                )) {

                    chip.setChecked(true);
                    break;
                }
            }
        }
    }

    private void selectLevel() {

        String level =
                userPrefs.getString(
                        "level",
                        ""
                );

        for (int i = 0;
             i < rgLevel.getChildCount();
             i++) {

            RadioButton radioButton =
                    (RadioButton)
                            rgLevel.getChildAt(i);

            if (radioButton
                    .getText()
                    .toString()
                    .equals(level)) {

                radioButton.setChecked(true);
                break;
            }
        }
    }

    private void selectPace() {

        String pace =
                userPrefs.getString(
                        "pace",
                        ""
                );

        for (int i = 0;
             i < togglePace.getChildCount();
             i++) {

            MaterialButton button =
                    (MaterialButton)
                            togglePace.getChildAt(i);

            if (button
                    .getText()
                    .toString()
                    .equals(pace)) {

                togglePace.check(
                        button.getId()
                );

                break;
            }
        }
    }

    private void saveProfile() {

        String firstName =
                etFirstName.getText()
                        .toString()
                        .trim();

        String lastName =
                etLastName.getText()
                        .toString()
                        .trim();

        String email =
                etEmail.getText()
                        .toString()
                        .trim();

        if (TextUtils.isEmpty(firstName)) {

            tilFirstName.setError(
                    "Le prénom est requis"
            );

            return;
        }

        tilFirstName.setError(null);

        if (TextUtils.isEmpty(lastName)) {

            tilLastName.setError(
                    "Le nom est requis"
            );

            return;
        }

        tilLastName.setError(null);

        if (TextUtils.isEmpty(email)) {

            tilEmail.setError(
                    "L'e-mail est requis"
            );

            return;
        }

        if (!Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()) {

            tilEmail.setError(
                    "E-mail invalide"
            );

            return;
        }

        tilEmail.setError(null);

        if (cgObjectives.getCheckedChipId() == -1) {

            Toast.makeText(
                    this,
                    "Choisissez un objectif.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (cgInterests.getCheckedChipIds().isEmpty()) {

            Toast.makeText(
                    this,
                    "Choisissez au moins un intérêt.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (rgLevel.getCheckedRadioButtonId() == -1) {

            Toast.makeText(
                    this,
                    "Choisissez votre niveau.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (togglePace.getCheckedButtonId() == -1) {

            Toast.makeText(
                    this,
                    "Choisissez votre rythme.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        Chip objectiveChip =
                findViewById(
                        cgObjectives.getCheckedChipId()
                );

        String objective =
                objectiveChip
                        .getText()
                        .toString();

        StringBuilder interests =
                new StringBuilder();

        List<Integer> interestIds =
                cgInterests.getCheckedChipIds();

        for (Integer id : interestIds) {

            Chip chip =
                    findViewById(id);

            interests
                    .append(
                            chip.getText().toString()
                    )
                    .append(",");
        }

        RadioButton levelButton =
                findViewById(
                        rgLevel.getCheckedRadioButtonId()
                );

        String level =
                levelButton
                        .getText()
                        .toString();

        MaterialButton paceButton =
                findViewById(
                        togglePace.getCheckedButtonId()
                );

        String pace =
                paceButton
                        .getText()
                        .toString();

        SharedPreferences.Editor editor =
                userPrefs.edit();

        editor.putString(
                "firstName",
                firstName
        );

        editor.putString(
                "lastName",
                lastName
        );

        editor.putString(
                "email",
                email
        );

        editor.putString(
                "objective",
                objective
        );

        editor.putString(
                "interests",
                interests.toString()
        );

        editor.putString(
                "level",
                level
        );

        editor.putString(
                "pace",
                pace
        );

        if (selectedPhotoUri != null) {

            editor.putString(
                    "profilePhotoUri",
                    selectedPhotoUri.toString()
            );
        }

        editor.apply();

        Toast.makeText(
                this,
                "Profil mis à jour.",
                Toast.LENGTH_SHORT
        ).show();

        finish();
    }
}