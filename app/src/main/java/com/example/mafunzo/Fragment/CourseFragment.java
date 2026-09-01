package com.example.mafunzo.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mafunzo.Adapter.CourseAdapter;
import com.example.mafunzo.Model.Course;
import com.example.mafunzo.R;
import com.example.mafunzo.Utils.CourseRepository;

import java.util.List;

public class CourseFragment extends Fragment {

    private RecyclerView recyclerCourses;
    private EditText etSearchCourse;

    private CourseAdapter courseAdapter;

    public CourseFragment() {
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
                R.layout.fragment_course,
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

        recyclerCourses =
                view.findViewById(
                        R.id.recycler_courses
                );

        etSearchCourse =
                view.findViewById(
                        R.id.et_search_course
                );

        recyclerCourses.setLayoutManager(
                new LinearLayoutManager(
                        requireContext()
                )
        );

        List<Course> courses =
                CourseRepository.getCourses();

        courseAdapter =
                new CourseAdapter(
                        requireContext(),
                        courses
                );

        recyclerCourses.setAdapter(
                courseAdapter
        );

        etSearchCourse.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after
                    ) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count
                    ) {

                        courseAdapter.filter(
                                s.toString()
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s
                    ) {
                    }
                }
        );
    }

    @Override
    public void onResume() {

        super.onResume();

        if (courseAdapter != null) {
            courseAdapter.refreshProgress();
        }
    }
}