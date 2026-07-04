package com.example.backend.models;

public class Trainer {
    private int id; // userId
    private String firstName;
    private String lastName;
    private String image;
    private String specialization;
    private double pricePerHour;
    private int facilityId;
    private String facilityName;
    private double averageRating;

    public Trainer() {}

    public Trainer(int id, String firstName, String lastName, String image,
                    String specialization, double pricePerHour,
                    int facilityId, String facilityName, double averageRating) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
        this.specialization = specialization;
        this.pricePerHour = pricePerHour;
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.averageRating = averageRating;
    }
    
    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

}
