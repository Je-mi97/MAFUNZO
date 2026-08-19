package com.example.mafunzo.Activity;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mafunzo.R;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_payment
        );

        ImageButton btnBack =
                findViewById(
                        R.id.btn_back_payment
                );

        btnBack.setOnClickListener(
                v -> finish()
        );
    }
}