package com.example.mafunzo.Model;

import android.content.Context;
import android.net.Uri;
import android.media.MediaMetadataRetriever;

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

    private final int estimatedDurationMinutes;

    private boolean completed;

    public Module(
            String id,
            String title,
            String description,
            String contentType,
            String resource,
            int estimatedDurationMinutes
    ) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.contentType = contentType;
        this.resource = resource;
        this.estimatedDurationMinutes =
                estimatedDurationMinutes;
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

    public int getEstimatedDurationMinutes() {
        return estimatedDurationMinutes;
    }

    public long getDurationSeconds(
            Context context
    ) {

        // Pour une vidéo, on récupère la durée réelle.
        if (TYPE_VIDEO.equals(
                contentType
        )) {

            long duration =
                    getVideoDurationSeconds(
                            context
                    );

            if (duration > 0) {
                return duration;
            }
        }

        // PDF / QUIZ ou vidéo illisible :
        // on utilise la durée estimée.
        return estimatedDurationMinutes * 60L;
    }

    private long getVideoDurationSeconds(
            Context context
    ) {

        MediaMetadataRetriever retriever =
                new MediaMetadataRetriever();

        try {

            int resourceId =
                    context.getResources()
                            .getIdentifier(
                                    resource,
                                    "raw",
                                    context.getPackageName()
                            );

            if (resourceId == 0) {
                return 0;
            }

            Uri uri =
                    Uri.parse(
                            "android.resource://"
                                    + context.getPackageName()
                                    + "/"
                                    + resourceId
                    );

            retriever.setDataSource(
                    context,
                    uri
            );

            String duration =
                    retriever.extractMetadata(
                            MediaMetadataRetriever
                                    .METADATA_KEY_DURATION
                    );

            if (duration == null) {
                return 0;
            }

            long milliseconds =
                    Long.parseLong(duration);

            return (
                    milliseconds + 999
            ) / 1000;

        } catch (Exception e) {

            return 0;

        } finally {

            try {
                retriever.release();
            } catch (Exception ignored) {
            }
        }
    }

    public String getFormattedDuration(
            Context context
    ) {

        long seconds =
                getDurationSeconds(
                        context
                );

        long minutes =
                seconds / 60;

        long remainingSeconds =
                seconds % 60;

        if (minutes == 0) {

            return remainingSeconds
                    + " sec";
        }

        if (remainingSeconds == 0) {

            return minutes
                    + " min";
        }

        return minutes
                + " min "
                + remainingSeconds
                + " sec";
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