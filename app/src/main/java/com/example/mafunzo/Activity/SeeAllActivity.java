package com.example.mafunzo.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mafunzo.Adapter.CourseAdapter;
import com.example.mafunzo.Adapter.SubjectAdapter;
import com.example.mafunzo.Model.Course;
import com.example.mafunzo.R;
import com.example.mafunzo.Utils.CourseRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SeeAllActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.see_all_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar_see_all);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String category = getIntent().getStringExtra("CATEGORY");
        RecyclerView recyclerView = findViewById(R.id.rv_see_all);

        if (category != null) {
            updateUI(category, recyclerView);
        }
    }

    private void updateUI(String category, RecyclerView recyclerView) {
        List<Course> allCourses = CourseRepository.getCourses();
        
        switch (category) {
            case "subjects":
                setTitle(getString(R.string.section_subjects));
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                Set<String> categories = new HashSet<>();
                for (Course c : allCourses) {
                    categories.add(c.getCategory());
                }
                recyclerView.setAdapter(new SubjectAdapter(new ArrayList<>(categories), false));
                break;
                
            case "recommendations":
            case "all_courses":
                setTitle(category.equals("recommendations") ? getString(R.string.section_recommendations) : getString(R.string.nav_courses));
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                recyclerView.setAdapter(new CourseAdapter(this, allCourses));
                break;
                
            case "careers":
                setTitle(getString(R.string.section_careers));
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                List<String> careers = new ArrayList<>();
                careers.add("Développeur Android (En dév)");
                careers.add("Data Scientist (En dév)");
                careers.add("Designer UX (En dév)");
                recyclerView.setAdapter(new SubjectAdapter(careers, true));
                break;
                
            case "certifications":
                setTitle(getString(R.string.section_certifications));
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                List<String> certs = new ArrayList<>();
                certs.add("Certification Java (Bientôt)");
                certs.add("Certification Web (Bientôt)");
                recyclerView.setAdapter(new SubjectAdapter(certs, true));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
