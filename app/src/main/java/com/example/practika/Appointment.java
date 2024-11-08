package com.example.practika;

public class Appointment {
    private String name;
    private String phone;
    private String service;
    private String date;
    private String time;

    public Appointment(String name, String phone, String service, String date, String time) {
        this.name = name;
        this.phone = phone;
        this.service = service;
        this.date = date;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getService() {
        return service;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}