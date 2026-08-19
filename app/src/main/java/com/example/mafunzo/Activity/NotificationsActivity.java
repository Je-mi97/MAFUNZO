package com.example.mafunzo.Activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mafunzo.Model.NotificationItem;
import com.example.mafunzo.R;
import com.example.mafunzo.Utils.AppNotificationManager;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class NotificationsActivity
        extends AppCompatActivity {

    private LinearLayout container;

    private AppNotificationManager notificationManager;

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_notifications
        );

        ImageButton btnBack =
                findViewById(
                        R.id.btn_back_notifications
                );

        container =
                findViewById(
                        R.id.notifications_container
                );

        notificationManager =
                new AppNotificationManager(
                        this
                );

        btnBack.setOnClickListener(
                v -> finish()
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        displayNotifications();
    }

    private void displayNotifications() {

        container.removeAllViews();

        List<NotificationItem> notifications =
                notificationManager
                        .getNotificationHistory();

        if (notifications.isEmpty()) {

            TextView empty =
                    new TextView(this);

            empty.setText(
                    "Aucune notification pour le moment."
            );

            empty.setTextColor(
                    getColor(
                            R.color.text_secondary
                    )
            );

            empty.setTextSize(16);

            empty.setPadding(
                    8,
                    24,
                    8,
                    24
            );

            container.addView(
                    empty
            );

            return;
        }

        for (NotificationItem item :
                notifications) {

            addNotification(item);
        }
    }

    private void addNotification(
            NotificationItem item
    ) {

        MaterialCardView card =
                new MaterialCardView(this);

        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(
                LinearLayout.VERTICAL
        );

        layout.setPadding(
                18,
                18,
                18,
                18
        );

        TextView title =
                new TextView(this);

        title.setText(
                item.getTitle()
        );

        title.setTextColor(
                getColor(
                        R.color.green_dark
                )
        );

        title.setTextSize(17);

        title.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        TextView message =
                new TextView(this);

        message.setText(
                item.getMessage()
        );

        message.setTextColor(
                getColor(
                        R.color.text_primary
                )
        );

        message.setTextSize(14);

        message.setPadding(
                0,
                6,
                0,
                0
        );

        TextView date =
                new TextView(this);

        date.setText(
                item.getDate()
        );

        date.setTextColor(
                getColor(
                        R.color.text_secondary
                )
        );

        date.setTextSize(12);

        date.setPadding(
                0,
                8,
                0,
                0
        );

        layout.addView(title);
        layout.addView(message);
        layout.addView(date);

        card.addView(layout);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(
                0,
                0,
                0,
                12
        );

        card.setLayoutParams(params);

        card.setRadius(18);
        card.setCardElevation(2);

        container.addView(card);
    }
}