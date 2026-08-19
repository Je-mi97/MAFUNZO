package com.example.mafunzo.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.example.mafunzo.R;
import com.example.mafunzo.Utils.AppNotificationManager;
import com.example.mafunzo.Utils.ProgressManager;

public class VideoActivity extends AppCompatActivity {

    private PlayerView playerView;
    private ExoPlayer player;

    private String videoName;
    private String courseId;
    private String moduleId;

    private long savedPosition = 0L;
    private boolean positionRestored = false;

    @Override
    protected void onCreate(
            @Nullable Bundle savedInstanceState
    ) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_video
        );

        ImageButton btnBack =
                findViewById(
                        R.id.btn_back_video
                );

        playerView =
                findViewById(
                        R.id.player_view
                );

        btnBack.setOnClickListener(
                v -> finish()
        );

        videoName =
                getIntent().getStringExtra(
                        "video_name"
                );

        courseId =
                getIntent().getStringExtra(
                        "course_id"
                );

        moduleId =
                getIntent().getStringExtra(
                        "module_id"
                );

        if (videoName == null
                || videoName.trim().isEmpty()) {

            finish();
            return;
        }

        loadSavedPosition();
    }

    @Override
    protected void onStart() {
        super.onStart();

        initializePlayer();
    }

    private String getProgressKey() {

        return courseId
                + "_"
                + moduleId
                + "_video_position";
    }

    private void loadSavedPosition() {

        savedPosition =
                getSharedPreferences(
                        "VideoProgress",
                        MODE_PRIVATE
                )
                        .getLong(
                                getProgressKey(),
                                0L
                        );
    }

    private void initializePlayer() {

        int resourceId =
                getResources().getIdentifier(
                        videoName,
                        "raw",
                        getPackageName()
                );

        if (resourceId == 0) {
            return;
        }

        player =
                new ExoPlayer.Builder(
                        this
                ).build();

        playerView.setPlayer(
                player
        );

        Uri videoUri =
                Uri.parse(
                        "android.resource://"
                                + getPackageName()
                                + "/"
                                + resourceId
                );

        MediaItem mediaItem =
                MediaItem.fromUri(
                        videoUri
                );

        player.setMediaItem(
                mediaItem
        );

        player.addListener(
                new Player.Listener() {

                    @Override
                    public void onPlaybackStateChanged(
                            int playbackState
                    ) {

                        if (playbackState ==
                                Player.STATE_READY) {

                            restorePosition();

                            player.play();
                        }

                        if (playbackState ==
                                Player.STATE_ENDED) {

                            clearSavedPosition();

                            markVideoAsCompleted();
                        }
                    }

                    @Override
                    public void onPlayerError(
                            PlaybackException error
                    ) {

                        Toast.makeText(
                                VideoActivity.this,
                                "Erreur de lecture vidéo.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );

        player.prepare();
    }

    private void restorePosition() {

        if (positionRestored) {
            return;
        }

        positionRestored = true;

        if (savedPosition <= 0) {
            return;
        }

        long duration =
                player.getDuration();

        if (duration <= 0
                || savedPosition >= duration) {

            clearSavedPosition();
            return;
        }

        player.seekTo(
                savedPosition
        );
    }

    private void saveCurrentPosition() {

        if (player == null) {
            return;
        }

        if (player.getPlaybackState()
                == Player.STATE_ENDED) {

            return;
        }

        long position =
                player.getCurrentPosition();

        if (position <= 0) {
            return;
        }

        getSharedPreferences(
                "VideoProgress",
                MODE_PRIVATE
        )
                .edit()
                .putLong(
                        getProgressKey(),
                        position
                )
                .apply();
    }

    private void clearSavedPosition() {

        getSharedPreferences(
                "VideoProgress",
                MODE_PRIVATE
        )
                .edit()
                .remove(
                        getProgressKey()
                )
                .apply();
    }

    private void markVideoAsCompleted() {

        if (courseId == null
                || moduleId == null) {

            return;
        }

        ProgressManager progressManager =
                new ProgressManager(this);

        progressManager.notifyVideoCompleted(
                courseId,
                moduleId
        );
    }

    @Override
    protected void onStop() {

        saveCurrentPosition();
        releasePlayer();

        super.onStop();
    }

    private void releasePlayer() {

        if (player != null) {

            player.release();
            player = null;
        }

        if (playerView != null) {

            playerView.setPlayer(null);
        }

        positionRestored = false;
    }
}