package com.example.mafunzo.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.mafunzo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity
        extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private final ActivityResultLauncher<String>
            notificationPermissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                        // La décision de l'utilisateur est conservée
                        // par Android.
                    }
            );

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_home
        );

        bottomNavigationView =
                findViewById(
                        R.id.bottom_navigation
                );

        NavHostFragment navHostFragment =
                (NavHostFragment)
                        getSupportFragmentManager()
                                .findFragmentById(
                                        R.id.nav_host_fragment
                                );

        if (navHostFragment != null) {

            NavController navController =
                    navHostFragment
                            .getNavController();

            NavigationUI.setupWithNavController(
                    bottomNavigationView,
                    navController
            );
        }

        requestNotificationPermission();
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