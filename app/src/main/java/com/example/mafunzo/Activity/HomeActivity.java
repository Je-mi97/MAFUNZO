package com.example.mafunzo.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.mafunzo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private final ActivityResultLauncher<String>
            notificationPermissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                        // Rien à faire immédiatement.
                    }
            );

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_home
        );

        // Correction : On applique les insets du bas pour que la BottomNav ne soit pas cachée par les touches
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home_main_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupNavigation();

        requestNotificationPermission();
    }

    private void setupNavigation() {

        BottomNavigationView bottomNavigationView =
                findViewById(
                        R.id.bottom_navigation
                );

        NavHostFragment navHostFragment =
                (NavHostFragment)
                        getSupportFragmentManager()
                                .findFragmentById(
                                        R.id.nav_host_fragment
                                );

        if (navHostFragment == null) {
            return;
        }

        NavController navController =
                navHostFragment.getNavController();

        NavigationUI.setupWithNavController(
                bottomNavigationView,
                navController
        );
    }

    private void requestNotificationPermission() {

        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.TIRAMISU) {

            if (checkSelfPermission(
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                notificationPermissionLauncher.launch(
                        Manifest.permission.POST_NOTIFICATIONS
                );
            }
        }
    }
}