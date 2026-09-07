package com.example.mafunzo.Activity;

import android.os.Bundle;
import android.graphics.Typeface;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mafunzo.Model.Course;
import com.example.mafunzo.Model.Module;
import com.example.mafunzo.R;
import com.example.mafunzo.Utils.CourseRepository;
import com.example.mafunzo.Utils.ProgressManager;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private LinearLayout historyContainer;

    private ProgressManager progressManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_history
        );

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.history_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        historyContainer =
                findViewById(
                        R.id.history_container
                );

        ImageButton btnBack =
                findViewById(
                        R.id.btn_back_history
                );

        progressManager =
                new ProgressManager(this);

        btnBack.setOnClickListener(
                v -> finish()
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        displayHistory();
    }

    private void displayHistory() {

        historyContainer.removeAllViews();

        List<Course> courses =
                CourseRepository.getCourses();

        boolean hasHistory = false;

        for (Course course : courses) {

            for (Module module :
                    course.getModules()) {

                if (progressManager.isModuleCompleted(
                        course.getId(),
                        module.getId()
                )) {

                    hasHistory = true;

                    addHistoryItem(
                            course,
                            module
                    );
                }
            }
        }

        if (!hasHistory) {

            TextView emptyView =
                    new TextView(this);

            emptyView.setText(
                    "Aucun module terminé pour le moment."
            );

            emptyView.setTextColor(
                    getColor(
                            R.color.text_secondary
                    )
            );

            emptyView.setTextSize(16);

            emptyView.setPadding(
                    8,
                    24,
                    8,
                    24
            );

            historyContainer.addView(
                    emptyView
            );
        }
    }

    private void addHistoryItem(
            Course course,
            Module module
    ) {

        MaterialCardView card =
                new MaterialCardView(this);

        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(
                LinearLayout.VERTICAL
        );

        layout.setPadding(
                18,
                18,
                18,
                18
        );

        TextView title =
                new TextView(this);

        title.setText(
                module.getTitle()
        );

        title.setTextColor(
                getColor(
                        R.color.green_dark
                )
        );

        title.setTextSize(17);

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        TextView courseName =
                new TextView(this);

        courseName.setText(
                course.getTitle()
        );

        courseName.setTextColor(
                getColor(
                        R.color.text_secondary
                )
        );

        courseName.setTextSize(14);

        courseName.setPadding(
                0,
                6,
                0,
                0
        );

        TextView date =
                new TextView(this);

        date.setText(
                "Terminé le "
                        + progressManager
                        .getCompletionDate(
                                course.getId(),
                                module.getId()
                        )
        );

        date.setTextColor(
                getColor(
                        R.color.green_primary
                )
        );

        date.setTextSize(13);

        date.setPadding(
                0,
                8,
                0,
                0
        );

        layout.addView(title);
        layout.addView(courseName);
        layout.addView(date);

        card.addView(layout);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(
                0,
                0,
                0,
                12
        );

        card.setLayoutParams(params);

        card.setRadius(18);
        card.setCardElevation(2);

        historyContainer.addView(card);
    }
}