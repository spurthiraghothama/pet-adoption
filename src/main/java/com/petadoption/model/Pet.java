package com.petadoption.model;

import jakarta.persistence.*;
//import com.petadoption.model.Status;


@Entity
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petId;
    private String name;
    private String species;
    private int age;
    private boolean vaccinationStatus;
    @Enumerated(EnumType.STRING)
    private Status availabilityStatus;
    private String healthStatus;
    private String imageUrl;

    public Pet() {}

    public Pet(Long petId, String name, String species, int age, boolean vaccinationStatus, Status availabilityStatus, String healthStatus, String imageUrl) {
        this.petId = petId;
        this.name = name;
        this.species = species;
        this.age = age;
        this.vaccinationStatus = vaccinationStatus;
        this.availabilityStatus = availabilityStatus;
        this.healthStatus = healthStatus;
        this.imageUrl = imageUrl;
    }

    public static PetBuilder builder() {
        return new PetBuilder();
    }

    public static class PetBuilder {
        private Long petId;
        private String name;
        private String species;
        private int age;
        private boolean vaccinationStatus;
        private Status availabilityStatus;
        private String healthStatus;
        private String imageUrl;

        public PetBuilder petId(Long petId) { this.petId = petId; return this; }
        public PetBuilder name(String name) { this.name = name; return this; }
        public PetBuilder species(String species) { this.species = species; return this; }
        public PetBuilder age(int age) { this.age = age; return this; }
        public PetBuilder vaccinationStatus(boolean vaccinationStatus) { this.vaccinationStatus = vaccinationStatus; return this; }
        public PetBuilder availabilityStatus(Status availabilityStatus) { this.availabilityStatus = availabilityStatus; return this; }
        public PetBuilder healthStatus(String healthStatus) { this.healthStatus = healthStatus; return this; }
        public PetBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }

        public Pet build() {
            return new Pet(petId, name, species, age, vaccinationStatus, availabilityStatus, healthStatus, imageUrl);
        }
    }

    // Getters and Setters
    public Long getPetId() { return petId; }
    public void setPetId(Long petId) { this.petId = petId; }
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
    public String getHealthStatus() { return healthStatus; }
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
