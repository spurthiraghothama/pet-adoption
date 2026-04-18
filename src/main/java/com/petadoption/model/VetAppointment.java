package com.petadoption.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class VetAppointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;
    
    private LocalDate date;
    private LocalTime time;

    @Enumerated(EnumType.STRING)
    private VetAppointmentStatus status;

    private String appointmentType; // VISIT / VET_CHECK

    @ManyToOne
    @JoinColumn(name = "pet_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "password"})
    private User user;

    public VetAppointment() {}

    public VetAppointment(Long appointmentId, LocalDate date, LocalTime time,
                       VetAppointmentStatus status, String appointmentType, Pet pet, User user) {
        this.appointmentId = appointmentId;
        this.date = date;
        this.time = time;
        this.status = status;
        this.appointmentType = appointmentType;
        this.pet = pet;
        this.user = user;
    }

    public static VetAppointmentBuilder builder() {
        return new VetAppointmentBuilder();
    }

    public static class VetAppointmentBuilder {
        private Long appointmentId;
        private LocalDate date;
        private LocalTime time;
        private VetAppointmentStatus status;
        private String appointmentType;
        private Pet pet;
        private User user;

        public VetAppointmentBuilder appointmentId(Long appointmentId) { this.appointmentId = appointmentId; return this; }
        public VetAppointmentBuilder date(LocalDate date) { this.date = date; return this; }
        public VetAppointmentBuilder time(LocalTime time) { this.time = time; return this; }
        public VetAppointmentBuilder status(VetAppointmentStatus status) { this.status = status; return this; }
        public VetAppointmentBuilder appointmentType(String appointmentType) { this.appointmentType = appointmentType; return this; }
        public VetAppointmentBuilder pet(Pet pet) { this.pet = pet; return this; }
        public VetAppointmentBuilder user(User user) { this.user = user; return this; }

        public VetAppointment build() {
            return new VetAppointment(appointmentId, date, time, status, appointmentType, pet, user);
        }
    }

    // Getters & Setters
    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }
    public VetAppointmentStatus getStatus() { return status; }
    public void setStatus(VetAppointmentStatus status) { this.status = status; }
    public String getAppointmentType() { return appointmentType; }
    public void setAppointmentType(String appointmentType) { this.appointmentType = appointmentType; }
    public Pet getPet() { return pet; }
    public void setPet(Pet pet) { this.pet = pet; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
