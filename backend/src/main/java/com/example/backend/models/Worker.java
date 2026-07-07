package com.example.backend.models;

public class Worker extends User{
    private String facilityName;
    private String address;
    private String registrationNumber;
    private String taxId;
     private Integer facilityId;

    public Integer getFacilityId() {
        return facilityId;
    }

     public void setFacilityId(Integer facilityId) {
         this.facilityId = facilityId;
     }

    public Worker() {}

    public Worker(int id, String username, String password, String firstname, String lastname, 
                  String email, String phone, String profileImage,
                  String facilityName, String address, String registrationNumber, String taxId) {
        
        super(id, username, password, firstname, lastname, email, phone, profileImage, "APPROVED", "WORKER");
        this.facilityName = facilityName;
        this.address = address;
        this.registrationNumber = registrationNumber;
        this.taxId = taxId;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    
    


}
