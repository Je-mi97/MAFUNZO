package com.example.mafunzo.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mafunzo.Model.Course;
import com.example.mafunzo.Model.Module;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProgressManager {

    private static final String PREFS_NAME = "CourseProgress";

    private final SharedPreferences preferences;
    private final Context context;

    public ProgressManager(Context context) {

        this.context = context.getApplicationContext();

        preferences = this.context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
        );
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

        notifyModuleCompleted(
                courseId,
                moduleId
        );
    }

    public boolean markModuleCompletedSilently(
            String courseId,
            String moduleId
    ) {

        String key = courseId + "_" + moduleId;

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
                ).format(new Date());

        preferences.edit()
                .putBoolean(key, true)
                .putString(key + "_date", date)
                .apply();

        return true;
    }

    private void notifyModuleCompleted(
            String courseId,
            String moduleId
    ) {

        SharedPreferences settings =
                context.getSharedPreferences(
                        "AppSettings",
                        Context.MODE_PRIVATE
                );

        boolean notificationsEnabled =
                settings.getBoolean(
                        "notificationsEnabled",
                        true
                );

        if (!notificationsEnabled) {
            return;
        }

        Course course =
                CourseRepository.getCourseById(
                        courseId
                );

        if (course == null) {
            return;
        }

        Module completedModule = null;

        for (Module module : course.getModules()) {

            if (module.getId().equals(moduleId)) {
                completedModule = module;
                break;
            }
        }

        if (completedModule == null) {
            return;
        }

        AppNotificationManager manager =
                new AppNotificationManager(context);

        manager.showNotification(
                "Module terminé 🎉",
                course.getTitle()
                        + " — "
                        + completedModule.getTitle(),
                true
        );
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

        sendSpecificNotification(
                "Vidéo terminée 🎥",
                courseId,
                moduleId
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

        SharedPreferences settings =
                context.getSharedPreferences(
                        "AppSettings",
                        Context.MODE_PRIVATE
                );

        boolean notificationsEnabled =
                settings.getBoolean(
                        "notificationsEnabled",
                        true
                );

        if (!notificationsEnabled) {
            return;
        }

        Course course =
                CourseRepository.getCourseById(
                        courseId
                );

        if (course == null) {
            return;
        }

        Module module = findModule(
                course,
                moduleId
        );

        if (module == null) {
            return;
        }

        AppNotificationManager manager =
                new AppNotificationManager(context);

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

    private void sendSpecificNotification(
            String title,
            String courseId,
            String moduleId
    ) {

        SharedPreferences settings =
                context.getSharedPreferences(
                        "AppSettings",
                        Context.MODE_PRIVATE
                );

        boolean notificationsEnabled =
                settings.getBoolean(
                        "notificationsEnabled",
                        true
                );

        if (!notificationsEnabled) {
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
                new AppNotificationManager(context);

        manager.showNotification(
                title,
                course.getTitle()
                        + " — "
                        + module.getTitle(),
                true
        );
    }

    private Module findModule(
            Course course,
            String moduleId
    ) {

        for (Module module : course.getModules()) {

            if (module.getId().equals(moduleId)) {
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
                courseId + "_" + moduleId + "_date",
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

        for (Module module : course.getModules()) {

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