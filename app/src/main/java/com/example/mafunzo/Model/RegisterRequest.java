package com.example.mafunzo.Model;

public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;

    private String objective;
    private String interests;
    private String level;
    private String pace;

    public RegisterRequest(
            String firstName,
            String lastName,
            String email,
            String phone,
            String password,
            String objective,
            String interests,
            String level,
            String pace
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.objective = objective;
        this.interests = interests;
        this.level = level;
        this.pace = pace;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getObjective() {
        return objective;
    }

    public String getInterests() {
        return interests;
    }

    public String getLevel() {
        return level;
    }

    public String getPace() {
        return pace;
    }
}