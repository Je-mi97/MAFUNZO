package com.example.mafunzo.Model;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable {

    private final String id;
    private final String title;
    private final String category;
    private final String description;
    private final String instructor;

    // Prix en FC
    private final long priceFc;

    private int progress;

    private final List<Module> modules;

    public Course(
            String id,
            String title,
            String category,
            String description,
            String instructor,
            long priceFc
    ) {

        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.instructor = instructor;
        this.priceFc = priceFc;
        this.progress = 0;
        this.modules = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getInstructor() {
        return instructor;
    }

    public long getPriceFc() {
        return priceFc;
    }

    public boolean isFree() {
        return priceFc <= 0;
    }

    public String getFormattedPrice() {

        if (isFree()) {
            return "Gratuit";
        }

        return String.format(
                "%,d FC",
                priceFc
        );
    }

    // MODULES

    public List<Module> getModules() {
        return modules;
    }

    public void addModule(
            Module module
    ) {

        if (module == null) {
            return;
        }

        modules.add(module);
        calculateProgress();
    }

    // DURÉE DU COURS

    public long getDurationSeconds(
            Context context
    ) {

        long totalSeconds = 0;

        for (Module module : modules) {

            totalSeconds +=
                    module.getDurationSeconds(
                            context
                    );
        }

        return totalSeconds;
    }

    public String getFormattedDuration(
            Context context
    ) {

        long totalSeconds =
                getDurationSeconds(
                        context
                );

        long hours =
                totalSeconds / 3600;

        long minutes =
                (totalSeconds % 3600) / 60;

        if (hours > 0 && minutes > 0) {

            return hours
                    + " h "
                    + minutes
                    + " min";
        }

        if (hours > 0) {

            return hours + " h";
        }

        return minutes + " min";
    }

    // PROGRESSION

    public int getProgress() {

        calculateProgress();

        return progress;
    }

    public void calculateProgress() {

        if (modules.isEmpty()) {

            progress = 0;
            return;
        }

        int completedModules = 0;

        for (Module module : modules) {

            if (module.isCompleted()) {
                completedModules++;
            }
        }

        progress =
                (completedModules * 100)
                        / modules.size();
    }

    public boolean isCompleted() {

        calculateProgress();

        return progress == 100;
    }
}