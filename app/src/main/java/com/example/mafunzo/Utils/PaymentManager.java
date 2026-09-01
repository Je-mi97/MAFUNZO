package com.example.mafunzo.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mafunzo.Model.Course;
import com.example.mafunzo.Model.PaymentTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PaymentManager {

    private static final String PREFS_NAME =
            "CoursePayments";

    private static final String KEY_TRANSACTION_COUNT =
            "transaction_count";

    private final SharedPreferences preferences;

    public PaymentManager(Context context) {

        preferences =
                context.getApplicationContext()
                        .getSharedPreferences(
                                PREFS_NAME,
                                Context.MODE_PRIVATE
                        );
    }

    // ACHAT

    public void purchaseCourse(
            Course course,
            String paymentMethod
    ) {

        if (course == null) {
            return;
        }

        String transactionId =
                generateTransactionId();

        long date =
                System.currentTimeMillis();

        saveTransaction(
                new PaymentTransaction(
                        transactionId,
                        course.getId(),
                        course.getTitle(),
                        paymentMethod,
                        course.getPriceFc(),
                        date,
                        PaymentTransaction.STATUS_SUCCESS
                )
        );
    }

    // GÉNÉRER UNE TRANSACTION

    private String generateTransactionId() {

        String randomPart =
                UUID.randomUUID()
                        .toString()
                        .replace(
                                "-",
                                ""
                        )
                        .substring(
                                0,
                                10
                        )
                        .toUpperCase();

        return "MFZ-" + randomPart;
    }

    // ENREGISTRER

    private void saveTransaction(
            PaymentTransaction transaction
    ) {

        int count =
                preferences.getInt(
                        KEY_TRANSACTION_COUNT,
                        0
                );

        SharedPreferences.Editor editor =
                preferences.edit();

        editor.putString(
                "transaction_id_" + count,
                transaction.getTransactionId()
        );

        editor.putString(
                "course_id_" + count,
                transaction.getCourseId()
        );

        editor.putString(
                "course_name_" + count,
                transaction.getCourseName()
        );

        editor.putString(
                "payment_method_" + count,
                transaction.getPaymentMethod()
        );

        editor.putLong(
                "amount_" + count,
                transaction.getAmount()
        );

        editor.putLong(
                "date_" + count,
                transaction.getDate()
        );

        editor.putString(
                "status_" + count,
                transaction.getStatus()
        );

        // On conserve aussi l'accès à la formation.
        editor.putBoolean(
                "course_" + transaction.getCourseId(),
                transaction.getStatus().equals(
                        PaymentTransaction.STATUS_SUCCESS
                )
        );

        editor.putInt(
                KEY_TRANSACTION_COUNT,
                count + 1
        );

        editor.apply();
    }

    // FORMATION ACHETÉE ?

    public boolean isCoursePurchased(
            String courseId
    ) {

        return preferences.getBoolean(
                "course_" + courseId,
                false
        );
    }

    // HISTORIQUE

    public List<PaymentTransaction>
    getTransactions() {

        List<PaymentTransaction> transactions =
                new ArrayList<>();

        int count =
                preferences.getInt(
                        KEY_TRANSACTION_COUNT,
                        0
                );

        for (int i = count - 1;
             i >= 0;
             i--) {

            String transactionId =
                    preferences.getString(
                            "transaction_id_" + i,
                            ""
                    );

            String courseId =
                    preferences.getString(
                            "course_id_" + i,
                            ""
                    );

            String courseName =
                    preferences.getString(
                            "course_name_" + i,
                            ""
                    );

            String paymentMethod =
                    preferences.getString(
                            "payment_method_" + i,
                            ""
                    );

            long amount =
                    preferences.getLong(
                            "amount_" + i,
                            0L
                    );

            long date =
                    preferences.getLong(
                            "date_" + i,
                            0L
                    );

            String status =
                    preferences.getString(
                            "status_" + i,
                            PaymentTransaction.STATUS_FAILED
                    );

            transactions.add(
                    new PaymentTransaction(
                            transactionId,
                            courseId,
                            courseName,
                            paymentMethod,
                            amount,
                            date,
                            status
                    )
            );
        }

        return transactions;
    }

    // TRANSACTION LA PLUS RÉCENTE

    public PaymentTransaction
    getLastTransaction() {

        List<PaymentTransaction> transactions =
                getTransactions();

        if (transactions.isEmpty()) {
            return null;
        }

        return transactions.get(0);
    }

    // SUPPRESSION D'UNE TRANSACTION

    public void clearAllTransactions() {

        preferences
                .edit()
                .clear()
                .apply();
    }
}