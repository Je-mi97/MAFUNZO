package com.example.mafunzo.Fragment;

import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.mafunzo.R;

public class HomeFragment extends Fragment {

    private RecyclerView rvSubjects, rvRecommendations;
    private final Handler autoScrollHandler = new Handler();
    private int scrollPosition = 0;

    public HomeFragment() {
        // Requis
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 1. Personnalisation du Header Premium
        TextView tvGreeting = view.findViewById(R.id.tv_greeting);
        SharedPreferences prefs = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String firstName = prefs.getString("firstName", "Étudiant");
        tvGreeting.setText(getString(R.string.greeting_hello, firstName));

        // 2. Initialisation des listes
        rvSubjects = view.findViewById(R.id.rv_subjects);
        rvRecommendations = view.findViewById(R.id.rv_recommendations);

        startAutoScroll();

        return view;
    }

    private void startAutoScroll() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (scrollPosition < 5) scrollPosition++;
                else scrollPosition = 0;
                
                if (rvSubjects != null && rvSubjects.getAdapter() != null) {
                    rvSubjects.smoothScrollToPosition(scrollPosition);
                }
                autoScrollHandler.postDelayed(this, 4000);
            }
        };
        autoScrollHandler.postDelayed(runnable, 4000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        autoScrollHandler.removeCallbacksAndMessages(null);
    }
}
