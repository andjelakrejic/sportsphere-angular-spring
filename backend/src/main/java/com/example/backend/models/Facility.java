package com.example.backend.models;

public class Facility { // ima mnogo vise polja u bazi
    private int id;
    private String name;
    private String city;
    private String address;
    private String description;
    private String workingHoursFrom;
    private String workingHoursTo;
    private double pricePerHour;
    private String status;
    private int numOfLikes;
    
    
    
    public Facility(int id, String name, String city, String address, String description, String workingHoursFrom,
            String workingHoursTo, double pricePerHour, String status, int numOfLikes) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.description = description;
        this.workingHoursFrom = workingHoursFrom;
        this.workingHoursTo = workingHoursTo;
        this.pricePerHour = pricePerHour;
        this.status = status;
        this.numOfLikes = numOfLikes;
    }

    // konstruktor bez numOfLikes - za getActiveFacilities
    public Facility(int id, String name, String city, String address,
                    String description, String workingHoursFrom, String workingHoursTo,
                    double pricePerHour, String status) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.description = description;
        this.workingHoursFrom = workingHoursFrom;
        this.workingHoursTo = workingHoursTo;
        this.pricePerHour = pricePerHour;
        this.status = status;
        this.numOfLikes = 0; // default
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getWorkingHoursFrom() {
        return workingHoursFrom;
    }
    public void setWorkingHoursFrom(String workingHoursFrom) {
        this.workingHoursFrom = workingHoursFrom;
    }
    public String getWorkingHoursTo() {
        return workingHoursTo;
    }
    public void setWorkingHoursTo(String workingHoursTo) {
        this.workingHoursTo = workingHoursTo;
    }
    public double getPricePerHour() {
        return pricePerHour;
    }
    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }



    public int getNumOfLikes() {
        return numOfLikes;
    }



    public void setNumOfLikes(int numOfLikes) {
        this.numOfLikes = numOfLikes;
    }  
    
    
}
