package com.example.mafunzo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mafunzo.R;

public class LoadingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(LoadingActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }
}
