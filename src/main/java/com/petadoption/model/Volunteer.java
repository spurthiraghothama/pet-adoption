package com.petadoption.model;

import jakarta.persistence.Entity;

@Entity
public class Volunteer extends User {
    private Boolean availabilityStatus = true; // Default to available
        
    public Boolean getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(Boolean availabilityStatus) { 
            this.availabilityStatus = availabilityStatus; 
    }

}

