package com.example.mafunzo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mafunzo.Activity.CourseDetailActivity;
import com.example.mafunzo.Model.Course;
import com.example.mafunzo.R;
import com.example.mafunzo.Utils.ProgressManager;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter
        extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private final Context context;
    private final List<Course> originalCourses;
    private final List<Course> filteredCourses;

    private final ProgressManager progressManager;

    public CourseAdapter(
            Context context,
            List<Course> courses
    ) {

        this.context = context;

        this.originalCourses =
                new ArrayList<>(courses);

        this.filteredCourses =
                new ArrayList<>(courses);

        this.progressManager =
                new ProgressManager(context);
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(context)
                        .inflate(
                                R.layout.item_course,
                                parent,
                                false
                        );

        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull CourseViewHolder holder,
            int position
    ) {

        Course course =
                filteredCourses.get(position);

        int progress =
                progressManager
                        .calculateCourseProgress(
                                course
                        );

        holder.tvCategory.setText(
                course.getCategory()
        );

        holder.tvTitle.setText(
                course.getTitle()
        );

        holder.tvDescription.setText(
                course.getDescription()
        );

        holder.progressBar.setProgress(
                progress
        );

        holder.tvProgress.setText(
                progress + "%"
        );

        holder.itemView.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            context,
                            CourseDetailActivity.class
                    );

            intent.putExtra(
                    "course",
                    course
            );

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return filteredCourses.size();
    }

    public void filter(String query) {

        filteredCourses.clear();

        if (query == null
                || query.trim().isEmpty()) {

            filteredCourses.addAll(
                    originalCourses
            );

        } else {

            String search =
                    query.toLowerCase().trim();

            for (Course course
                    : originalCourses) {

                if (
                        course.getTitle()
                                .toLowerCase()
                                .contains(search)
                                ||
                                course.getCategory()
                                        .toLowerCase()
                                        .contains(search)
                                ||
                                course.getDescription()
                                        .toLowerCase()
                                        .contains(search)
                ) {

                    filteredCourses.add(course);
                }
            }
        }

        notifyDataSetChanged();
    }

    public void refreshProgress() {
        notifyDataSetChanged();
    }

    public static class CourseViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvCategory;
        TextView tvDescription;
        TextView tvProgress;

        ProgressBar progressBar;

        public CourseViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            tvTitle =
                    itemView.findViewById(
                            R.id.tv_course_title
                    );

            tvCategory =
                    itemView.findViewById(
                            R.id.tv_course_category
                    );

            tvDescription =
                    itemView.findViewById(
                            R.id.tv_course_description
                    );

            tvProgress =
                    itemView.findViewById(
                            R.id.tv_course_progress
                    );

            progressBar =
                    itemView.findViewById(
                            R.id.progress_course
                    );
        }
    }
}