package com.example.mafunzo.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable {

    private final String id;
    private final String title;
    private final String category;
    private final String description;
    private final String instructor;
    private final int duration;

    private int progress;

    private final List<Module> modules;

    public Course(
            String id,
            String title,
            String category,
            String description,
            String instructor,
            int duration
    ) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.instructor = instructor;
        this.duration = duration;
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

    public int getDuration() {
        return duration;
    }

    public int getProgress() {
        calculateProgress();
        return progress;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void addModule(Module module) {
        modules.add(module);
        calculateProgress();
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