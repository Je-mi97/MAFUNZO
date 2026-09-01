package com.example.mafunzo.Model;

import java.io.Serializable;

public class PaymentTransaction implements Serializable {

    public static final String STATUS_SUCCESS =
            "SUCCESS";

    public static final String STATUS_PENDING =
            "PENDING";

    public static final String STATUS_FAILED =
            "FAILED";

    private final String transactionId;
    private final String courseId;
    private final String courseName;
    private final String paymentMethod;
    private final long amount;
    private final long date;
    private final String status;

    public PaymentTransaction(
            String transactionId,
            String courseId,
            String courseName,
            String paymentMethod,
            long amount,
            long date,
            String status
    ) {
        this.transactionId = transactionId;
        this.courseId = courseId;
        this.courseName = courseName;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public long getAmount() {
        return amount;
    }

    public long getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}