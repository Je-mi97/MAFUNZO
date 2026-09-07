package com.example.mafunzo.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mafunzo.Model.Module;
import com.example.mafunzo.R;
import com.example.mafunzo.Utils.ProgressManager;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ModuleDetailActivity extends AppCompatActivity {

    private String courseId;
    private Module module;
    private ProgressManager progressManager;

    private TextView tvTitle, tvType, tvDescription, tvToolbarTitle;
    private MaterialButton btnAction, btnComplete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.module_detail_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressManager = new ProgressManager(this);
        courseId = getIntent().getStringExtra("course_id");
        module = (Module) getIntent().getSerializableExtra("module");

        if (module == null || courseId == null) {
            finish();
            return;
        }

        tvToolbarTitle = findViewById(R.id.tv_module_toolbar_title);
        tvTitle = findViewById(R.id.tv_module_title);
        tvType = findViewById(R.id.tv_module_type);
        tvDescription = findViewById(R.id.tv_module_description);
        btnAction = findViewById(R.id.btn_action_module);
        btnComplete = findViewById(R.id.btn_complete_module);

        ImageButton btnBack = findViewById(R.id.btn_back_module);
        btnBack.setOnClickListener(v -> finish());

        displayModule();

        btnAction.setOnClickListener(v -> openModuleContent());
        btnComplete.setOnClickListener(v -> markAsCompleted());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (module != null && courseId != null) {
            boolean completed = progressManager.isModuleCompleted(courseId, module.getId());
            module.setCompleted(completed);
            displayModule();
        }
    }

    private void displayModule() {
        tvToolbarTitle.setText(module.getTitle());
        tvTitle.setText(module.getTitle());
        tvType.setText("Type : " + module.getContentType());
        tvDescription.setText(module.getDescription());

        if (module.isCompleted()) {
            btnComplete.setText("✓ Terminé");
            btnComplete.setEnabled(false);
        } else {
            btnComplete.setText("Marquer comme terminé");
            btnComplete.setEnabled(true);
        }

        // Configuration du bouton d'action selon le type
        switch (module.getContentType()) {
            case Module.TYPE_VIDEO:
                btnAction.setText("Regarder la vidéo");
                btnComplete.setVisibility(View.GONE); // Auto-terminé à la fin de la vidéo
                break;
            case Module.TYPE_QUIZ:
                btnAction.setText("Commencer le quiz");
                btnComplete.setVisibility(View.GONE); // Auto-terminé si score >= 60%
                break;
            case Module.TYPE_PDF:
                btnAction.setText("Lire le PDF");
                btnComplete.setVisibility(View.VISIBLE);
                break;
            default:
                btnAction.setText("Ouvrir le contenu");
                btnComplete.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void openModuleContent() {
        String type = module.getContentType();
        if (Module.TYPE_VIDEO.equals(type)) {
            Intent intent = new Intent(this, VideoActivity.class);
            intent.putExtra("video_resource", module.getResource());
            intent.putExtra("course_id", courseId);
            intent.putExtra("module_id", module.getId());
            startActivity(intent);
        } else if (Module.TYPE_QUIZ.equals(type)) {
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra("course_id", courseId);
            intent.putExtra("module_id", module.getId());
            startActivity(intent);
        } else if (Module.TYPE_PDF.equals(type)) {
            openPdfFromAssets(module.getResource());
        } else {
            Toast.makeText(this, "Contenu : " + module.getResource(), Toast.LENGTH_SHORT).show();
        }
    }

    private void openPdfFromAssets(String fileName) {
        try {
            File pdfFile = new File(getCacheDir(), fileName);
            if (!pdfFile.exists()) {
                InputStream is = getAssets().open("courses/" + fileName);
                FileOutputStream os = new FileOutputStream(pdfFile);
                byte[] buffer = new byte[4096];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.close();
                is.close();
            }

            Uri pdfUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", pdfFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(pdfUri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Erreur lors de l'ouverture du PDF.", Toast.LENGTH_LONG).show();
        }
    }

    private void markAsCompleted() {
        if (courseId != null) {
            progressManager.markModuleCompleted(courseId, module.getId());
            module.setCompleted(true);
            displayModule();
            Toast.makeText(this, "Module validé !", Toast.LENGTH_SHORT).show();
        }
    }
}
