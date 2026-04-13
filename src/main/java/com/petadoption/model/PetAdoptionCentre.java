package com.petadoption.model;

import jakarta.persistence.*;

@Entity
public class PetAdoptionCentre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long centreId;
    private String name;
    private String address;
    private String contactInfo;

    public PetAdoptionCentre() {}

    public Long getCentreId() { return centreId; }
    public void setCentreId(Long centreId) { this.centreId = centreId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
}
