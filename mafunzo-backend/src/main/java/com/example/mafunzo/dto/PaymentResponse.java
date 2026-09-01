package com.example.mafunzo.dto;

import com.example.mafunzo.model.Payment;

import java.time.LocalDateTime;

public class PaymentResponse {

    private String reference;

    private Long userId;

    private String courseId;

    private String courseName;

    private Double amount;

    private String currency;

    private Payment.PaymentMethod paymentMethod;

    private String phone;

    private Payment.PaymentStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public PaymentResponse() {
    }

    public PaymentResponse(
            Payment payment
    ) {

        this.reference =
                payment.getReference();

        this.userId =
                payment.getUserId();

        this.courseId =
                payment.getCourseId();

        this.courseName =
                payment.getCourseName();

        this.amount =
                payment.getAmount();

        this.currency =
                payment.getCurrency();

        this.paymentMethod =
                payment.getPaymentMethod();

        this.phone =
                payment.getPhone();

        this.status =
                payment.getStatus();

        this.createdAt =
                payment.getCreatedAt();

        this.updatedAt =
                payment.getUpdatedAt();
    }

    public String getReference() {
        return reference;
    }

    public Long getUserId() {
        return userId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public Double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Payment.PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public String getPhone() {
        return phone;
    }

    public Payment.PaymentStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}