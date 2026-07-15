package com.example.backend.models;

public class Promotion {
    private int id;
    private String name;
    private String facilityName;
    private String dateFrom;
    private String dateTo;
    private String discountType;  // PERCENTAGE ili FIXED
    private double discountValue;
    
    private int facilityId;
    private int sportId;
    private String sportName; 

    public Promotion() {}


    // koristi se za getPromotionsByFacility, addPromotion, updatePromotion
    public Promotion(int id, String name, int facilityId, String facilityName, int sportId, String sportName,
                      String dateFrom, String dateTo, String discountType, double discountValue) {
        this.id = id;
        this.name = name;
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.sportId = sportId;
        this.sportName = sportName;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.discountType = discountType;
        this.discountValue = discountValue;
    }
    
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

    public int getFacilityId() {
        return facilityId;
    }


    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }


    public int getSportId() {
        return sportId;
    }


    public void setSportId(int sportId) {
        this.sportId = sportId;
    }


    public String getSportName() {
        return sportName;
    }


    public void setSportName(String sportName) {
        this.sportName = sportName;
    }
    

}
