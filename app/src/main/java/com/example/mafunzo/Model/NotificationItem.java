package com.example.mafunzo.Model;

import java.io.Serializable;

public class NotificationItem implements Serializable {

    private final String title;
    private final String message;
    private final String date;

    public NotificationItem(
            String title,
            String message,
            String date
    ) {
        this.title = title;
        this.message = message;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }
}