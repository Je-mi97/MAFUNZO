package com.example.mafunzo.Activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mafunzo.Adapter.ModuleAdapter;
import com.example.mafunzo.Model.Course;
import com.example.mafunzo.Model.Module;
import com.example.mafunzo.R;
import com.example.mafunzo.Utils.ProgressManager;

public class CourseDetailActivity extends AppCompatActivity {

    private Course course;

    private TextView tvTitle;
    private TextView tvCategory;
    private TextView tvDescription;
    private TextView tvInstructor;
    private TextView tvDuration;
    private TextView tvProgress;

    private ProgressBar progressBar;

    private ModuleAdapter moduleAdapter;

    private ProgressManager progressManager;

    @Override
    protected void onCreate(
            @Nullable Bundle savedInstanceState
    ) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_course_detail
        );

        progressManager =
                new ProgressManager(this);

        ImageButton btnBack =
                findViewById(
                        R.id.btn_back_course
                );

        tvTitle =
                findViewById(
                        R.id.tv_detail_title
                );

        tvCategory =
                findViewById(
                        R.id.tv_detail_category
                );

        tvDescription =
                findViewById(
                        R.id.tv_detail_description
                );

        tvInstructor =
                findViewById(
                        R.id.tv_detail_instructor
                );

        tvDuration =
                findViewById(
                        R.id.tv_detail_duration
                );

        tvProgress =
                findViewById(
                        R.id.tv_detail_progress
                );

        progressBar =
                findViewById(
                        R.id.progress_detail
                );

        RecyclerView recyclerModules =
                findViewById(
                        R.id.recycler_modules
                );

        btnBack.setOnClickListener(
                v -> finish()
        );

        course =
                (Course) getIntent()
                        .getSerializableExtra(
                                "course"
                        );

        if (course == null) {
            finish();
            return;
        }

        recyclerModules.setLayoutManager(
                new LinearLayoutManager(this)
        );

        moduleAdapter =
                new ModuleAdapter(
                        this,
                        course
                );

        recyclerModules.setAdapter(
                moduleAdapter
        );

        displayCourse();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (course == null) {
            return;
        }

        displayCourse();

        if (moduleAdapter != null) {
            moduleAdapter.notifyDataSetChanged();
        }
    }

    private void displayCourse() {

        int progress =
                progressManager.calculateCourseProgress(
                        course
                );

        updateModulesState();

        tvTitle.setText(
                course.getTitle()
        );

        tvCategory.setText(
                course.getCategory()
        );

        tvDescription.setText(
                course.getDescription()
        );

        tvInstructor.setText(
                "Formateur : "
                        + course.getInstructor()
        );

        tvDuration.setText(
                "Durée : "
                        + course.getDuration()
                        + " heures"
        );

        tvProgress.setText(
                progress + "%"
        );

        progressBar.setProgress(
                progress
        );
    }

    private void updateModulesState() {

        for (Module module : course.getModules()) {

            boolean completed =
                    progressManager.isModuleCompleted(
                            course.getId(),
                            module.getId()
                    );

            module.setCompleted(
                    completed
            );
        }
    }
}