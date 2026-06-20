package com.example.backend.models;

public class Promotion {
    private int id;
    private String name;
    private String facilityName;  // iz JOIN-a
    private String dateFrom;
    private String dateTo;
    private String discountType;  // PERCENTAGE ili FIXED
    private double discountValue;
    
    public Promotion(int id, String name, String facilityName, String dateFrom, String dateTo, String discountType,
            double discountValue) {
        this.id = id;
        this.name = name;
        this.facilityName = facilityName;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.discountType = discountType;
        this.discountValue = discountValue;
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

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    

}
