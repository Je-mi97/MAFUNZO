package com.example.mafunzo.Activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mafunzo.Model.Course;
import com.example.mafunzo.R;
import com.example.mafunzo.Utils.CourseRepository;
import com.example.mafunzo.Utils.ProgressManager;

import java.util.List;

public class ProgressActivity extends AppCompatActivity {

    private ProgressManager progressManager;

    private TextView tvJavaProgress;
    private TextView tvWebProgress;
    private TextView tvMarketingProgress;

    private ProgressBar progressJava;
    private ProgressBar progressWeb;
    private ProgressBar progressMarketing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_progress
        );

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.progress_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressManager =
                new ProgressManager(this);

        ImageButton btnBack =
                findViewById(
                        R.id.btn_back_progress
                );

        tvJavaProgress =
                findViewById(
                        R.id.tv_java_progress
                );

        tvWebProgress =
                findViewById(
                        R.id.tv_web_progress
                );

        tvMarketingProgress =
                findViewById(
                        R.id.tv_marketing_progress
                );

        progressJava =
                findViewById(
                        R.id.progress_java
                );

        progressWeb =
                findViewById(
                        R.id.progress_web
                );

        progressMarketing =
                findViewById(
                        R.id.progress_marketing
                );

        btnBack.setOnClickListener(
                v -> finish()
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        displayProgress();
    }

    private void displayProgress() {

        List<Course> courses =
                CourseRepository.getCourses();

        if (courses.size() < 3) {
            return;
        }

        Course java = courses.get(0);
        Course web = courses.get(1);
        Course marketing = courses.get(2);

        int javaProgress =
                progressManager.calculateCourseProgress(
                        java
                );

        int webProgress =
                progressManager.calculateCourseProgress(
                        web
                );

        int marketingProgress =
                progressManager.calculateCourseProgress(
                        marketing
                );

        progressJava.setProgress(
                javaProgress
        );

        progressWeb.setProgress(
                webProgress
        );

        progressMarketing.setProgress(
                marketingProgress
        );

        tvJavaProgress.setText(
                javaProgress + "%"
        );

        tvWebProgress.setText(
                webProgress + "%"
        );

        tvMarketingProgress.setText(
                marketingProgress + "%"
        );
    }
}