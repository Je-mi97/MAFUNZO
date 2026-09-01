package com.example.mafunzo.Utils;

import com.example.mafunzo.Model.Course;
import com.example.mafunzo.Model.PaymentResult;

public interface PaymentRepository {

    PaymentResult pay(
            Course course,
            String paymentMethod
    );
}