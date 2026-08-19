package com.example.mafunzo.Model;

import java.io.Serializable;

public class Module implements Serializable {

    public static final String TYPE_PDF = "PDF";
    public static final String TYPE_VIDEO = "VIDEO";
    public static final String TYPE_QUIZ = "QUIZ";
    public static final String TYPE_TEXT = "TEXT";

    private final String id;
    private final String title;
    private final String description;
    private final String contentType;
    private final String resource;

    private boolean completed;

    public Module(
            String id,
            String title,
            String description,
            String contentType,
            String resource
    ) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.contentType = contentType;
        this.resource = resource;
        this.completed = false;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getContentType() {
        return contentType;
    }

    public String getResource() {
        return resource;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(
            boolean completed
    ) {
        this.completed = completed;
    }
}