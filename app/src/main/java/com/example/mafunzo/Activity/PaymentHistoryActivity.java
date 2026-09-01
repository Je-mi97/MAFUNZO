package com.example.mafunzo.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mafunzo.Model.PaymentTransaction;
import com.example.mafunzo.R;
import com.example.mafunzo.Utils.PaymentManager;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PaymentHistoryActivity
        extends AppCompatActivity {

    private LinearLayout container;

    private PaymentManager paymentManager;

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_payment_history
        );

        ImageButton btnBack =
                findViewById(
                        R.id.btn_back_payment_history
                );

        container =
                findViewById(
                        R.id.payment_history_container
                );

        paymentManager =
                new PaymentManager(this);

        btnBack.setOnClickListener(
                v -> finish()
        );
    }

    @Override
    protected void onResume() {

        super.onResume();

        displayTransactions();
    }

    private void displayTransactions() {

        container.removeAllViews();

        List<PaymentTransaction> transactions =
                paymentManager.getTransactions();

        if (transactions.isEmpty()) {

            displayEmptyState();

            return;
        }

        for (PaymentTransaction transaction :
                transactions) {

            addTransaction(
                    transaction
            );
        }
    }

    private void displayEmptyState() {

        TextView empty =
                new TextView(this);

        empty.setText(
                "Aucun paiement enregistré pour le moment."
        );

        empty.setTextColor(
                getColor(
                        R.color.text_secondary
                )
        );

        empty.setTextSize(16);

        empty.setGravity(
                android.view.Gravity.CENTER
        );

        empty.setPadding(
                10,
                40,
                10,
                40
        );

        container.addView(
                empty
        );
    }

    private void addTransaction(
            PaymentTransaction transaction
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
                createTextView(
                        transaction.getCourseName(),
                        17,
                        true
                );

        title.setTextColor(
                getColor(
                        R.color.green_dark
                )
        );

        TextView amount =
                createTextView(
                        "Montant : "
                                + formatAmount(
                                transaction.getAmount()
                        ),
                        15,
                        false
                );

        TextView method =
                createTextView(
                        "Moyen : "
                                + transaction.getPaymentMethod(),
                        14,
                        false
                );

        TextView date =
                createTextView(
                        "Date : "
                                + formatDate(
                                transaction.getDate()
                        ),
                        13,
                        false
                );

        TextView id =
                createTextView(
                        "Transaction : "
                                + transaction.getTransactionId(),
                        13,
                        false
                );

        TextView status =
                createTextView(
                        "Statut : Paiement réussi ✓",
                        13,
                        true
                );

        status.setTextColor(
                getColor(
                        R.color.success
                )
        );

        layout.addView(title);
        layout.addView(amount);
        layout.addView(method);
        layout.addView(date);
        layout.addView(id);
        layout.addView(status);

        card.addView(
                layout
        );

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

        card.setLayoutParams(
                params
        );

        card.setRadius(
                18
        );

        card.setCardElevation(
                2
        );

        container.addView(
                card
        );
    }

    private TextView createTextView(
            String text,
            int size,
            boolean bold
    ) {

        TextView textView =
                new TextView(this);

        textView.setText(
                text
        );

        textView.setTextSize(
                size
        );

        textView.setTextColor(
                getColor(
                        R.color.text_primary
                )
        );

        if (bold) {

            textView.setTypeface(
                    null,
                    android.graphics.Typeface.BOLD
            );
        }

        textView.setPadding(
                0,
                3,
                0,
                3
        );

        return textView;
    }

    private String formatAmount(
            long amount
    ) {

        return String.format(
                Locale.getDefault(),
                "%,d FC",
                amount
        );
    }

    private String formatDate(
            long timestamp
    ) {

        return new SimpleDateFormat(
                "dd/MM/yyyy HH:mm",
                Locale.getDefault()
        ).format(
                new Date(timestamp)
        );
    }
}