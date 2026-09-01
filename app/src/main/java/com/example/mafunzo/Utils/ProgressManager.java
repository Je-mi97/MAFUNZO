package com.example.mafunzo.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mafunzo.Model.Course;
import com.example.mafunzo.Model.Module;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProgressManager {

    private static final String PREFS_NAME =
            "CourseProgress";

    private final SharedPreferences preferences;
    private final Context context;

    private final AppSettings appSettings;

    public ProgressManager(Context context) {

        this.context =
                context.getApplicationContext();

        preferences =
                this.context.getSharedPreferences(
                        PREFS_NAME,
                        Context.MODE_PRIVATE
                );

        appSettings =
                new AppSettings(this.context);
    }

    public void markModuleCompleted(
            String courseId,
            String moduleId
    ) {

        boolean newlyCompleted =
                markModuleCompletedSilently(
                        courseId,
                        moduleId
                );

        if (!newlyCompleted) {
            return;
        }

        if (!appSettings.areNotificationsEnabled()) {
            return;
        }

        Course course =
                CourseRepository.getCourseById(
                        courseId
                );

        if (course == null) {
            return;
        }

        Module module =
                findModule(
                        course,
                        moduleId
                );

        if (module == null) {
            return;
        }

        AppNotificationManager manager =
                new AppNotificationManager(
                        context
                );

        manager.showNotification(
                "Module terminé 🎉",
                course.getTitle()
                        + " — "
                        + module.getTitle(),
                true
        );
    }

    public boolean markModuleCompletedSilently(
            String courseId,
            String moduleId
    ) {

        String key =
                courseId + "_" + moduleId;

        boolean alreadyCompleted =
                preferences.getBoolean(
                        key,
                        false
                );

        if (alreadyCompleted) {
            return false;
        }

        String date =
                new SimpleDateFormat(
                        "dd/MM/yyyy HH:mm",
                        Locale.getDefault()
                ).format(
                        new Date()
                );

        preferences.edit()
                .putBoolean(
                        key,
                        true
                )
                .putString(
                        key + "_date",
                        date
                )
                .apply();

        return true;
    }

    public void notifyVideoCompleted(
            String courseId,
            String moduleId
    ) {

        boolean newlyCompleted =
                markModuleCompletedSilently(
                        courseId,
                        moduleId
                );

        if (!newlyCompleted) {
            return;
        }

        if (!appSettings.areNotificationsEnabled()) {
            return;
        }

        Course course =
                CourseRepository.getCourseById(
                        courseId
                );

        if (course == null) {
            return;
        }

        Module module =
                findModule(
                        course,
                        moduleId
                );

        if (module == null) {
            return;
        }

        AppNotificationManager manager =
                new AppNotificationManager(
                        context
                );

        manager.showNotification(
                "Vidéo terminée 🎥",
                course.getTitle()
                        + " — "
                        + module.getTitle(),
                true
        );
    }

    public void notifyQuizPassed(
            String courseId,
            String moduleId,
            int percentage
    ) {

        boolean newlyCompleted =
                markModuleCompletedSilently(
                        courseId,
                        moduleId
                );

        if (!newlyCompleted) {
            return;
        }

        if (!appSettings.areNotificationsEnabled()) {
            return;
        }

        Course course =
                CourseRepository.getCourseById(
                        courseId
                );

        if (course == null) {
            return;
        }

        Module module =
                findModule(
                        course,
                        moduleId
                );

        if (module == null) {
            return;
        }

        AppNotificationManager manager =
                new AppNotificationManager(
                        context
                );

        manager.showNotification(
                "Quiz réussi 🎯",
                course.getTitle()
                        + " — "
                        + module.getTitle()
                        + " : "
                        + percentage
                        + " %",
                true
        );
    }

    private Module findModule(
            Course course,
            String moduleId
    ) {

        for (Module module :
                course.getModules()) {

            if (module.getId()
                    .equals(moduleId)) {

                return module;
            }
        }

        return null;
    }

    public boolean isModuleCompleted(
            String courseId,
            String moduleId
    ) {

        return preferences.getBoolean(
                courseId + "_" + moduleId,
                false
        );
    }

    public String getCompletionDate(
            String courseId,
            String moduleId
    ) {

        return preferences.getString(
                courseId + "_"
                        + moduleId
                        + "_date",
                ""
        );
    }

    public int calculateCourseProgress(
            Course course
    ) {

        if (course.getModules().isEmpty()) {
            return 0;
        }

        int completedModules = 0;

        for (Module module :
                course.getModules()) {

            if (isModuleCompleted(
                    course.getId(),
                    module.getId()
            )) {

                completedModules++;
            }
        }

        return (
                completedModules * 100
        ) / course.getModules().size();
    }
}