package com.example.practika;

public class ServiceItem {
    private String name;
    private String description;
    private String price;

    public ServiceItem(String name, String description, String price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }
}