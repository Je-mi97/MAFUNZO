package com.example.mafunzo.Utils;

import com.example.mafunzo.Model.Course;
import com.example.mafunzo.Model.PaymentResult;

public class PaymentService {

    private final PaymentRepository repository;

    public PaymentService(
            PaymentRepository repository
    ) {
        this.repository =
                repository;
    }

    public PaymentResult pay(
            Course course,
            String paymentMethod
    ) {

        return repository.pay(
                course,
                paymentMethod
        );
    }
}