package com.example.mafunzo.repository;

import com.example.mafunzo.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    Optional<Payment> findByReference(
            String reference
    );

    List<Payment> findByUserIdOrderByCreatedAtDesc(
            Long userId
    );

    List<Payment> findByUserIdAndStatus(
            Long userId,
            Payment.PaymentStatus status
    );

    boolean existsByUserIdAndCourseIdAndStatus(
            Long userId,
            String courseId,
            Payment.PaymentStatus status
    );
}