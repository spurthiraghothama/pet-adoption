package com.petadoption.model;

import jakarta.persistence.*;

@Entity
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petId;
    private String name;
    private String species;
    private int age;
    private boolean vaccinationStatus;
    private String availabilityStatus;

    public Pet() {}

    public Pet(Long petId, String name, String species, int age, boolean vaccinationStatus, String availabilityStatus) {
        this.petId = petId;
        this.name = name;
        this.species = species;
        this.age = age;
        this.vaccinationStatus = vaccinationStatus;
        this.availabilityStatus = availabilityStatus;
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
        private String availabilityStatus;

        public PetBuilder petId(Long petId) { this.petId = petId; return this; }
        public PetBuilder name(String name) { this.name = name; return this; }
        public PetBuilder species(String species) { this.species = species; return this; }
        public PetBuilder age(int age) { this.age = age; return this; }
        public PetBuilder vaccinationStatus(boolean vaccinationStatus) { this.vaccinationStatus = vaccinationStatus; return this; }
        public PetBuilder availabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; return this; }

        public Pet build() {
            return new Pet(petId, name, species, age, vaccinationStatus, availabilityStatus);
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
    public String getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; }
}
