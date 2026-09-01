package com.example.mafunzo.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.mafunzo.Model.Module;
import com.example.mafunzo.R;
import com.example.mafunzo.Utils.ProgressManager;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Locale;

public class ModuleDetailActivity extends AppCompatActivity {

    private Module module;
    private String courseId;

    private TextView tvTitle;
    private TextView tvDescription;
    private TextView tvType;

    private MaterialButton btnOpen;
    private MaterialButton btnComplete;

    private ProgressManager progressManager;

    @Override
    protected void onCreate(
            @Nullable Bundle savedInstanceState
    ) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_module_detail
        );

        progressManager =
                new ProgressManager(this);

        ImageButton btnBack =
                findViewById(
                        R.id.btn_back_module
                );

        tvTitle =
                findViewById(
                        R.id.tv_module_detail_title
                );

        tvDescription =
                findViewById(
                        R.id.tv_module_detail_description
                );

        tvType =
                findViewById(
                        R.id.tv_module_detail_type
                );

        btnOpen =
                findViewById(
                        R.id.btn_open_module
                );

        btnComplete =
                findViewById(
                        R.id.btn_complete_module
                );

        btnBack.setOnClickListener(
                v -> finish()
        );

        module =
                (Module) getIntent()
                        .getSerializableExtra(
                                "module"
                        );

        courseId =
                getIntent()
                        .getStringExtra(
                                "course_id"
                        );

        if (module == null || courseId == null) {

            Toast.makeText(
                    this,
                    "Module invalide.",
                    Toast.LENGTH_LONG
            ).show();

            finish();
            return;
        }

        displayModule();

        btnOpen.setOnClickListener(
                v -> openModule()
        );

        btnComplete.setOnClickListener(
                v -> markPdfAsCompleted()
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (module != null && courseId != null) {
            displayModule();
        }
    }

    private void displayModule() {

        tvTitle.setText(
                module.getTitle()
        );

        tvDescription.setText(
                module.getDescription()
        );

        String type =
                getModuleType();

        tvType.setText(
                "Type : " + type
        );

        boolean completed =
                progressManager.isModuleCompleted(
                        courseId,
                        module.getId()
                );

        module.setCompleted(
                completed
        );

        updateOpenButton(type);

        /*
         * PDF :
         * l'utilisateur doit cliquer sur
         * "Marquer comme terminé".
         *
         * VIDEO :
         * le module est terminé automatiquement
         * à la fin de la vidéo.
         *
         * QUIZ :
         * le module est terminé automatiquement
         * si le score est >= 60 %.
         */
        if (Module.TYPE_PDF.equals(type)) {

            btnComplete.setVisibility(
                    View.VISIBLE
            );

            if (completed) {

                btnComplete.setText(
                        "✓ Module terminé"
                );

                btnComplete.setEnabled(false);

            } else {

                btnComplete.setText(
                        "Marquer comme terminé"
                );

                btnComplete.setEnabled(true);
            }

        } else {

            btnComplete.setVisibility(
                    View.GONE
            );
        }
    }

    private String getModuleType() {

        if (module.getContentType() == null) {
            return "";
        }

        return module.getContentType()
                .trim()
                .toUpperCase(Locale.ROOT);
    }

    private void updateOpenButton(
            String type
    ) {

        switch (type) {

            case Module.TYPE_PDF:

                btnOpen.setText(
                        "Lire le PDF"
                );

                break;

            case Module.TYPE_VIDEO:

                btnOpen.setText(
                        "Regarder la vidéo"
                );

                break;

            case Module.TYPE_QUIZ:

                btnOpen.setText(
                        "Commencer le quiz"
                );

                break;

            default:

                btnOpen.setText(
                        "Ouvrir le contenu"
                );

                break;
        }
    }

    private void openModule() {

        String type =
                getModuleType();

        switch (type) {

            case Module.TYPE_PDF:

                openPdfFromAssets(
                        module.getResource()
                );

                break;

            case Module.TYPE_VIDEO:

                openVideo();

                break;

            case Module.TYPE_QUIZ:

                openQuiz();

                break;

            default:

                Toast.makeText(
                        this,
                        "Type de contenu non pris en charge.",
                        Toast.LENGTH_LONG
                ).show();

                break;
        }
    }

    private void openVideo() {

        Intent intent =
                new Intent(
                        this,
                        VideoActivity.class
                );

        intent.putExtra(
                "video_resource",
                module.getResource()
        );

        intent.putExtra(
                "course_id",
                courseId
        );

        intent.putExtra(
                "module_id",
                module.getId()
        );

        startActivity(intent);
    }

    private void openQuiz() {

        Intent intent =
                new Intent(
                        this,
                        QuizActivity.class
                );

        intent.putExtra(
                "course_id",
                courseId
        );

        intent.putExtra(
                "module_id",
                module.getId()
        );

        startActivity(intent);
    }

    private void markPdfAsCompleted() {

        progressManager.markModuleCompleted(
                courseId,
                module.getId()
        );

        module.setCompleted(true);

        btnComplete.setText(
                "✓ Module terminé"
        );

        btnComplete.setEnabled(false);

        Toast.makeText(
                this,
                "Module terminé !",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void openPdfFromAssets(
            String fileName
    ) {

        try {

            File pdfFile =
                    new File(
                            getCacheDir(),
                            fileName
                    );

            if (!pdfFile.exists()) {

                InputStream inputStream =
                        getAssets().open(
                                "courses/" + fileName
                        );

                FileOutputStream outputStream =
                        new FileOutputStream(
                                pdfFile
                        );

                byte[] buffer =
                        new byte[4096];

                int length;

                while (
                        (length =
                                inputStream.read(
                                        buffer
                                )) > 0
                ) {

                    outputStream.write(
                            buffer,
                            0,
                            length
                    );
                }

                outputStream.close();
                inputStream.close();
            }

            Uri pdfUri =
                    FileProvider.getUriForFile(
                            this,
                            getPackageName()
                                    + ".fileprovider",
                            pdfFile
                    );

            Intent intent =
                    new Intent(
                            Intent.ACTION_VIEW
                    );

            intent.setDataAndType(
                    pdfUri,
                    "application/pdf"
            );

            intent.addFlags(
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            );

            intent.addFlags(
                    Intent.FLAG_ACTIVITY_NO_HISTORY
            );

            startActivity(intent);

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Impossible d'ouvrir le PDF.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}