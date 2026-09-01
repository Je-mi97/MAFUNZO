package com.example.mafunzo.Utils;

import com.example.mafunzo.Model.Course;
import com.example.mafunzo.Model.PaymentResult;
import com.example.mafunzo.Model.PaymentTransaction;

import java.util.UUID;

public class LocalPaymentRepository
        implements PaymentRepository {

    private final PaymentManager paymentManager;

    public LocalPaymentRepository(
            PaymentManager paymentManager
    ) {
        this.paymentManager =
                paymentManager;
    }

    @Override
    public PaymentResult pay(
            Course course,
            String paymentMethod
    ) {

        if (course == null) {

            return new PaymentResult(
                    false,
                    PaymentResult.STATUS_FAILED,
                    null,
                    "Formation introuvable."
            );
        }

        if (course.isFree()) {

            return new PaymentResult(
                    true,
                    PaymentResult.STATUS_SUCCESS,
                    null,
                    "Formation gratuite."
            );
        }

        String transactionId =
                "MFZ-"
                        + UUID.randomUUID()
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

        paymentManager.purchaseCourse(
                course,
                paymentMethod
        );

        PaymentTransaction transaction =
                paymentManager
                        .getLastTransaction();

        if (transaction != null) {

            transactionId =
                    transaction
                            .getTransactionId();
        }

        return new PaymentResult(
                true,
                PaymentResult.STATUS_SUCCESS,
                transactionId,
                "Paiement simulé avec succès."
        );
    }
}