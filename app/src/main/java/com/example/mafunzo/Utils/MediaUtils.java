package com.example.mafunzo.Utils;

import android.content.Context;
import android.net.Uri;
import android.media.MediaMetadataRetriever;

public class MediaUtils {

    private MediaUtils() {
        // Classe utilitaire
    }

    public static long getVideoDurationSeconds(
            Context context,
            String resourceName
    ) {

        MediaMetadataRetriever retriever =
                new MediaMetadataRetriever();

        try {

            int resourceId =
                    context.getResources()
                            .getIdentifier(
                                    resourceName,
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

            String durationString =
                    retriever.extractMetadata(
                            MediaMetadataRetriever.METADATA_KEY_DURATION
                    );

            if (durationString == null) {
                return 0;
            }

            long durationMilliseconds =
                    Long.parseLong(durationString);

            return Math.max(
                    0,
                    (durationMilliseconds + 999) / 1000
            );

        } catch (Exception e) {

            return 0;

        } finally {

            try {
                retriever.release();
            } catch (Exception ignored) {
            }
        }
    }
}