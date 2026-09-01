package com.example.mafunzo.Activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mafunzo.Model.Course;
import com.example.mafunzo.Model.PaymentResult;
import com.example.mafunzo.Model.PaymentTransaction;
import com.example.mafunzo.R;
import com.example.mafunzo.Utils.AppNotificationManager;
import com.example.mafunzo.Utils.CourseRepository;
import com.example.mafunzo.Utils.LocalPaymentRepository;
import com.example.mafunzo.Utils.PaymentManager;
import com.example.mafunzo.Utils.PaymentService;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaymentActivity
        extends AppCompatActivity {

    private Course course;

    private TextView tvCourseName;
    private TextView tvPrice;
    private TextView tvPaymentStatus;
    private TextView tvTransactionId;

    private RadioGroup radioPaymentMethods;

    private RadioButton rbOrange;
    private RadioButton rbMpesa;
    private RadioButton rbAirtel;

    private MaterialButton btnPay;

    private PaymentManager paymentManager;
    private PaymentService paymentService;

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_payment
        );

        paymentManager =
                new PaymentManager(this);

        paymentService =
                new PaymentService(
                        new LocalPaymentRepository(
                                paymentManager
                        )
                );

        ImageButton btnBack =
                findViewById(
                        R.id.btn_back_payment
                );

        tvCourseName =
                findViewById(
                        R.id.tv_payment_course_name
                );

        tvPrice =
                findViewById(
                        R.id.tv_payment_price
                );

        tvPaymentStatus =
                findViewById(
                        R.id.tv_payment_status
                );

        tvTransactionId =
                findViewById(
                        R.id.tv_transaction_id
                );

        radioPaymentMethods =
                findViewById(
                        R.id.radio_payment_methods
                );

        rbOrange =
                findViewById(
                        R.id.rb_orange
                );

        rbMpesa =
                findViewById(
                        R.id.rb_mpesa
                );

        rbAirtel =
                findViewById(
                        R.id.rb_airtel
                );

        btnPay =
                findViewById(
                        R.id.btn_simulate_payment
                );

        btnBack.setOnClickListener(
                v -> finish()
        );

        String courseId =
                getIntent()
                        .getStringExtra(
                                "course_id"
                        );

        if (courseId == null
                || courseId.trim().isEmpty()) {

            showErrorAndClose(
                    "Formation introuvable."
            );

            return;
        }

        course =
                CourseRepository.getCourseById(
                        courseId
                );

        if (course == null) {

            showErrorAndClose(
                    "Formation introuvable."
            );

            return;
        }

        displayPayment();

        btnPay.setOnClickListener(
                v -> processPayment()
        );
    }

    private void displayPayment() {

        tvCourseName.setText(
                course.getTitle()
        );

        tvPrice.setText(
                course.getFormattedPrice()
        );

        PaymentTransaction transaction =
                findExistingTransaction();

        if (transaction != null) {

            showAlreadyPurchased(
                    transaction
            );

            return;
        }

        tvPaymentStatus.setText(
                "Paiement sécurisé — simulation"
        );

        tvPaymentStatus.setTextColor(
                getColor(
                        R.color.text_secondary
                )
        );

        tvTransactionId.setVisibility(
                TextView.GONE
        );

        btnPay.setText(
                "Simuler le paiement"
        );

        btnPay.setEnabled(
                true
        );

        radioPaymentMethods.setVisibility(
                RadioGroup.VISIBLE
        );
    }

    private PaymentTransaction
    findExistingTransaction() {

        for (PaymentTransaction transaction :
                paymentManager.getTransactions()) {

            if (transaction
                    .getCourseId()
                    .equals(course.getId())
                    &&
                    transaction
                            .getStatus()
                            .equals(
                                    PaymentTransaction.STATUS_SUCCESS
                            )) {

                return transaction;
            }
        }

        return null;
    }

    private void showAlreadyPurchased(
            PaymentTransaction transaction
    ) {

        tvPaymentStatus.setText(
                "✓ Formation déjà achetée"
        );

        tvPaymentStatus.setTextColor(
                getColor(
                        R.color.success
                )
        );

        tvTransactionId.setVisibility(
                TextView.VISIBLE
        );

        tvTransactionId.setText(
                "Transaction : "
                        + transaction.getTransactionId()
                        + "\n"
                        + "Méthode : "
                        + transaction.getPaymentMethod()
                        + "\n"
                        + "Date : "
                        + formatDate(
                        transaction.getDate()
                )
        );

        btnPay.setText(
                "Formation déjà débloquée"
        );

        btnPay.setEnabled(
                false
        );

        radioPaymentMethods.setVisibility(
                RadioGroup.GONE
        );
    }

    private void processPayment() {

        int selectedId =
                radioPaymentMethods
                        .getCheckedRadioButtonId();

        if (selectedId == -1) {

            Toast.makeText(
                    this,
                    "Choisissez un moyen de paiement.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String paymentMethod;

        if (selectedId ==
                rbOrange.getId()) {

            paymentMethod =
                    "Orange Money";

        } else if (
                selectedId ==
                        rbMpesa.getId()
        ) {

            paymentMethod =
                    "M-Pesa";

        } else if (
                selectedId ==
                        rbAirtel.getId()
        ) {

            paymentMethod =
                    "Airtel Money";

        } else {

            Toast.makeText(
                    this,
                    "Moyen de paiement invalide.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        btnPay.setEnabled(false);

        radioPaymentMethods.setEnabled(
                false
        );

        btnPay.setText(
                "Traitement..."
        );

        tvPaymentStatus.setText(
                "Traitement du paiement..."
        );

        btnPay.postDelayed(
                () -> completePayment(
                        paymentMethod
                ),
                1500
        );
    }

    private void completePayment(
            String paymentMethod
    ) {

        PaymentResult result =
                paymentService.pay(
                        course,
                        paymentMethod
                );

        if (!result.isSuccess()) {

            tvPaymentStatus.setText(
                    "✗ Paiement échoué"
            );

            tvPaymentStatus.setTextColor(
                    getColor(
                            R.color.error
                    )
            );

            btnPay.setText(
                    "Réessayer"
            );

            btnPay.setEnabled(
                    true
            );

            radioPaymentMethods.setEnabled(
                    true
            );

            return;
        }

        PaymentTransaction transaction =
                paymentManager
                        .getLastTransaction();

        AppNotificationManager manager =
                new AppNotificationManager(
                        this
                );

        manager.showNotification(
                "Paiement confirmé ✅",
                course.getTitle()
                        + " est maintenant débloqué.",
                true
        );

        tvPaymentStatus.setText(
                "✓ Paiement réussi"
        );

        tvPaymentStatus.setTextColor(
                getColor(
                        R.color.success
                )
        );

        if (transaction != null) {

            tvTransactionId.setVisibility(
                    TextView.VISIBLE
            );

            tvTransactionId.setText(
                    "Transaction : "
                            + transaction
                            .getTransactionId()
                            + "\n"
                            + "Méthode : "
                            + transaction
                            .getPaymentMethod()
                            + "\n"
                            + "Date : "
                            + formatDate(
                            transaction.getDate()
                    )
            );
        }

        btnPay.setText(
                "Formation débloquée"
        );

        btnPay.setEnabled(false);

        radioPaymentMethods.setVisibility(
                RadioGroup.GONE
        );

        Toast.makeText(
                this,
                result.getMessage(),
                Toast.LENGTH_LONG
        ).show();
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

    private void showErrorAndClose(
            String message
    ) {

        Toast.makeText(
                this,
                message,
                Toast.LENGTH_LONG
        ).show();

        finish();
    }
}