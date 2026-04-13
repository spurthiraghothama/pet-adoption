package com.petadoption.dto;

public class PetDTO {
    private String name;
    private String species;
    private int age;
    private boolean vaccinationStatus;
    private String availabilityStatus;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public boolean isVaccinationStatus() { return vaccinationStatus; }
    public void setVaccinationStatus(boolean vaccinationStatus) { this.vaccinationStatus = vaccinationStatus; }
    public String getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; }
}
