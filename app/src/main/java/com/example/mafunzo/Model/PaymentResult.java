package com.example.mafunzo.Model;

import java.io.Serializable;

public class PaymentResult implements Serializable {

    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_FAILED = "FAILED";

    private final boolean success;
    private final String status;
    private final String transactionId;
    private final String message;

    public PaymentResult(
            boolean success,
            String status,
            String transactionId,
            String message
    ) {
        this.success = success;
        this.status = status;
        this.transactionId = transactionId;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getStatus() {
        return status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getMessage() {
        return message;
    }
}