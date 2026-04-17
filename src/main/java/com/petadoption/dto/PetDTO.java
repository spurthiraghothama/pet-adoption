package com.petadoption.dto;
import com.petadoption.model.Status;


public class PetDTO {
    private String name;
    private String species;
    private int age;
    private boolean vaccinationStatus;
    private Status availabilityStatus;
    private String imageUrl;
    private String healthStatus;
    private Long registeredById;
    private String registeredByType;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public boolean isVaccinationStatus() { return vaccinationStatus; }
    public void setVaccinationStatus(boolean vaccinationStatus) { this.vaccinationStatus = vaccinationStatus; }
    public Status getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(Status availabilityStatus) { this.availabilityStatus = availabilityStatus; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getHealthStatus() { return healthStatus; }
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }
    public Long getRegisteredById() { return registeredById; }
    public void setRegisteredById(Long registeredById) { this.registeredById = registeredById; }
    public String getRegisteredByType() { return registeredByType; }
    public void setRegisteredByType(String registeredByType) { this.registeredByType = registeredByType; }
}
