package com.example.mafunzo.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mafunzo.Activity.SeeAllActivity;
import com.example.mafunzo.Adapter.CourseAdapter;
import com.example.mafunzo.Adapter.SubjectAdapter;
import com.example.mafunzo.Model.Course;
import com.example.mafunzo.R;
import com.example.mafunzo.Utils.CourseRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeFragment extends Fragment {

    private RecyclerView rvSubjects, rvRecommendations, rvCareers, rvCertifications;
    private final Handler autoScrollHandler = new Handler();
    private int scrollPosition = 0;

    public HomeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 1. Header Premium
        TextView tvGreeting = view.findViewById(R.id.tv_greeting);
        SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String firstName = prefs.getString("firstName", "Étudiant");
        tvGreeting.setText(getString(R.string.greeting_hello, firstName));

        // 2. Initialisation des RecyclerViews
        rvSubjects = view.findViewById(R.id.rv_subjects);
        rvRecommendations = view.findViewById(R.id.rv_recommendations);
        rvCareers = view.findViewById(R.id.rv_careers);
        rvCertifications = view.findViewById(R.id.rv_certifications);

        loadActualData();
        setupNavigation(view);
        startAutoScroll();

        return view;
    }

    private void loadActualData() {
        List<Course> allCourses = CourseRepository.getCourses();

        // Section SUJETS (Les catégories des 3 cours)
        Set<String> categories = new HashSet<>();
        for (Course c : allCourses) categories.add(c.getCategory());
        rvSubjects.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvSubjects.setAdapter(new SubjectAdapter(new ArrayList<>(categories), false));

        // Section RECOMMANDATIONS (Les 3 cours actuels en grille)
        rvRecommendations.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvRecommendations.setAdapter(new CourseAdapter(getContext(), allCourses));

        // Section CARRIÈRES (En développement)
        List<String> careers = new ArrayList<>();
        careers.add("Android Dev");
        careers.add("Web Dev");
        careers.add("Marketing");
        rvCareers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvCareers.setAdapter(new SubjectAdapter(careers, true));

        // Section CERTIFICATIONS (En développement)
        List<String> certs = new ArrayList<>();
        certs.add("Certificat Java");
        certs.add("Certificat Web");
        rvCertifications.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCertifications.setAdapter(new SubjectAdapter(certs, true));
    }

    private void setupNavigation(View view) {
        view.findViewById(R.id.btn_see_all_subjects).setOnClickListener(v -> openSeeAll("subjects"));
        view.findViewById(R.id.btn_see_all_recommendations).setOnClickListener(v -> openSeeAll("recommendations"));
        view.findViewById(R.id.btn_see_all_careers).setOnClickListener(v -> openSeeAll("careers"));
        view.findViewById(R.id.btn_see_all_certifications).setOnClickListener(v -> openSeeAll("certifications"));
    }

    private void openSeeAll(String cat) {
        Intent intent = new Intent(getActivity(), SeeAllActivity.class);
        intent.putExtra("CATEGORY", cat);
        startActivity(intent);
    }

    private void startAutoScroll() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                if (scrollPosition < 2) scrollPosition++; else scrollPosition = 0;
                if (rvSubjects != null) rvSubjects.smoothScrollToPosition(scrollPosition);
                autoScrollHandler.postDelayed(this, 5000);
            }
        };
        autoScrollHandler.postDelayed(r, 5000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        autoScrollHandler.removeCallbacksAndMessages(null);
    }
}
