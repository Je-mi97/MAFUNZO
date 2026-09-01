package com.example.mafunzo.Activity;

import android.content.Intent;
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
import com.example.mafunzo.Utils.PaymentManager;
import com.example.mafunzo.Utils.ProgressManager;
import com.google.android.material.button.MaterialButton;

public class CourseDetailActivity
        extends AppCompatActivity {

    private Course course;

    private TextView tvTitle;
    private TextView tvCategory;
    private TextView tvDescription;
    private TextView tvInstructor;
    private TextView tvDuration;
    private TextView tvPrice;
    private TextView tvProgress;

    private ProgressBar progressBar;

    private MaterialButton btnPurchase;

    private ModuleAdapter moduleAdapter;

    private ProgressManager progressManager;
    private PaymentManager paymentManager;

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

        paymentManager =
                new PaymentManager(this);

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

        tvPrice =
                findViewById(
                        R.id.tv_detail_price
                );

        tvProgress =
                findViewById(
                        R.id.tv_detail_progress
                );

        progressBar =
                findViewById(
                        R.id.progress_detail
                );

        btnPurchase =
                findViewById(
                        R.id.btn_purchase_course
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

        btnPurchase.setOnClickListener(
                v -> openPayment()
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

        updateModulesState();

        int progress =
                progressManager
                        .calculateCourseProgress(
                                course
                        );

        boolean purchased =
                course.isFree()
                        || paymentManager
                        .isCoursePurchased(
                                course.getId()
                        );

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
                        + course.getFormattedDuration(
                        this
                )
        );

        tvProgress.setText(
                progress + "%"
        );

        progressBar.setProgress(
                progress
        );

        if (course.isFree()) {

            tvPrice.setText(
                    "Gratuit"
            );

            btnPurchase.setVisibility(
                    MaterialButton.GONE
            );

        } else if (purchased) {

            tvPrice.setText(
                    "✓ Formation achetée"
            );

            btnPurchase.setVisibility(
                    MaterialButton.VISIBLE
            );

            btnPurchase.setText(
                    "✓ Formation débloquée"
            );

            btnPurchase.setEnabled(
                    false
            );

        } else {

            tvPrice.setText(
                    course.getFormattedPrice()
            );

            btnPurchase.setVisibility(
                    MaterialButton.VISIBLE
            );

            btnPurchase.setText(
                    "Acheter la formation"
            );

            btnPurchase.setEnabled(
                    true
            );
        }
    }

    private void updateModulesState() {

        for (Module module :
                course.getModules()) {

            boolean completed =
                    progressManager
                            .isModuleCompleted(
                                    course.getId(),
                                    module.getId()
                            );

            module.setCompleted(
                    completed
            );
        }
    }

    private void openPayment() {

        Intent intent =
                new Intent(
                        this,
                        PaymentActivity.class
                );

        intent.putExtra(
                "course_id",
                course.getId()
        );

        startActivity(intent);
    }
}