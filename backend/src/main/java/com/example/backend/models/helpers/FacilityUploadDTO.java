package com.example.backend.models.helpers;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FacilityUploadDTO {
    private String name;
    private String city;
    private String address;
    private String description;
    private String workingHoursFrom;
    private String workingHoursTo;
    private double pricePerHour;
    private int maxNoShows;
    private List<CourtDTO> courts; // moze biti prazna lista

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

    public int getMaxNoShows() {
        return maxNoShows;
    }

    public void setMaxNoShows(int maxNoShows) {
        this.maxNoShows = maxNoShows;
    }

    public List<CourtDTO> getCourts() {
        return courts;
    }

    public void setCourts(List<CourtDTO> courts) {
        this.courts = courts;
    }

    public static class CourtDTO {
        private String name;
        private String type;
        private int capacity;
        private String equipmentDescription;
        
        @JsonProperty("sportId")
        private int sportId;
        
        // getters i setters
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
        public int getCapacity() {
            return capacity;
        }
        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }
        public String getEquipmentDescription() {
            return equipmentDescription;
        }
        public void setEquipmentDescription(String equipmentDescription) {
            this.equipmentDescription = equipmentDescription;
        }
        public int getSportId() {
            return sportId;
        }
        public void setSportId(int sportId) {
            this.sportId = sportId;
        }
    }
    
}

