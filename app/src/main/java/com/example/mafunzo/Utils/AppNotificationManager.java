package com.example.mafunzo.Utils;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mafunzo.Model.NotificationItem;
import com.example.mafunzo.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppNotificationManager {

    public static final String CHANNEL_LEARNING =
            "learning_notifications";

    public static final String CHANNEL_GENERAL =
            "general_notifications";

    private static final String PREFS_NAME =
            "UserNotifications";

    private final Context context;
    private final SharedPreferences preferences;
    private final AppSettings appSettings;

    public AppNotificationManager(Context context) {

        this.context =
                context.getApplicationContext();

        this.preferences =
                this.context.getSharedPreferences(
                        PREFS_NAME,
                        Context.MODE_PRIVATE
                );

        this.appSettings =
                new AppSettings(this.context);

        createNotificationChannels();
    }

    private void createNotificationChannels() {

        if (Build.VERSION.SDK_INT <
                Build.VERSION_CODES.O) {

            return;
        }

        NotificationManager manager =
                (NotificationManager)
                        context.getSystemService(
                                Context.NOTIFICATION_SERVICE
                        );

        if (manager == null) {
            return;
        }

        NotificationChannel learningChannel =
                new NotificationChannel(
                        CHANNEL_LEARNING,
                        "Apprentissage",
                        NotificationManager.IMPORTANCE_HIGH
                );

        learningChannel.setDescription(
                "Notifications concernant les formations, vidéos et quiz."
        );

        NotificationChannel generalChannel =
                new NotificationChannel(
                        CHANNEL_GENERAL,
                        "Général",
                        NotificationManager.IMPORTANCE_DEFAULT
                );

        generalChannel.setDescription(
                "Informations générales de MAFUNZO."
        );

        manager.createNotificationChannel(
                learningChannel
        );

        manager.createNotificationChannel(
                generalChannel
        );
    }

    public void showNotification(
            String title,
            String message,
            boolean learning
    ) {

        // Réglage interne de MAFUNZO
        if (!appSettings.areNotificationsEnabled()) {
            return;
        }

        // Permission Android 13+
        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.TIRAMISU) {

            if (context.checkSelfPermission(
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
        }

        // Historique interne
        saveToHistory(
                title,
                message
        );

        String channelId =
                learning
                        ? CHANNEL_LEARNING
                        : CHANNEL_GENERAL;

        int notificationId =
                (int) System.currentTimeMillis();

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(
                        context,
                        channelId
                )
                        .setSmallIcon(
                                R.drawable.ic_notification
                        )
                        .setContentTitle(
                                title
                        )
                        .setContentText(
                                message
                        )
                        .setStyle(
                                new NotificationCompat.BigTextStyle()
                                        .bigText(message)
                        )
                        .setPriority(
                                learning
                                        ? NotificationCompat.PRIORITY_HIGH
                                        : NotificationCompat.PRIORITY_DEFAULT
                        )
                        .setAutoCancel(true);

        NotificationManagerCompat manager =
                NotificationManagerCompat.from(
                        context
                );

        manager.notify(
                notificationId,
                builder.build()
        );
    }

    private void saveToHistory(
            String title,
            String message
    ) {

        String date =
                new SimpleDateFormat(
                        "dd/MM/yyyy HH:mm",
                        Locale.getDefault()
                ).format(
                        new Date()
                );

        int count =
                preferences.getInt(
                        "count",
                        0
                );

        preferences.edit()
                .putString(
                        "title_" + count,
                        title
                )
                .putString(
                        "message_" + count,
                        message
                )
                .putString(
                        "date_" + count,
                        date
                )
                .putInt(
                        "count",
                        count + 1
                )
                .apply();
    }

    public List<NotificationItem>
    getNotificationHistory() {

        List<NotificationItem>
                notifications =
                new ArrayList<>();

        int count =
                preferences.getInt(
                        "count",
                        0
                );

        for (int i = count - 1;
             i >= 0;
             i--) {

            String title =
                    preferences.getString(
                            "title_" + i,
                            ""
                    );

            String message =
                    preferences.getString(
                            "message_" + i,
                            ""
                    );

            String date =
                    preferences.getString(
                            "date_" + i,
                            ""
                    );

            notifications.add(
                    new NotificationItem(
                            title,
                            message,
                            date
                    )
            );
        }

        return notifications;
    }

    public void clearNotificationHistory() {

        preferences
                .edit()
                .clear()
                .apply();
    }
}