package com.example.practika;

public class User {
    private String fullName;
    private String birthDate;
    private String gender;
    private Appointment appointment;

    public User(String fullName, String birthDate, String gender, Appointment appointment) {
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.appointment = appointment;
    }

    // Геттеры и сеттеры
    public String getFullName() {
        return fullName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getGender() {
        return gender;
    }

    public Appointment getAppointment() {
        return appointment;
    }
}