package com.petadoption.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;
    
    private LocalDate date;
    private LocalTime time;
    private String status;
    private String appointmentType; // e.g., "VISIT", "VET_CHECK"

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Appointment() {}

    public Appointment(Long appointmentId, LocalDate date, LocalTime time, String status, String appointmentType, Pet pet, User user) {
        this.appointmentId = appointmentId;
        this.date = date;
        this.time = time;
        this.status = status;
        this.appointmentType = appointmentType;
        this.pet = pet;
        this.user = user;
    }

    public static AppointmentBuilder builder() {
        return new AppointmentBuilder();
    }

    public static class AppointmentBuilder {
        private Long appointmentId;
        private LocalDate date;
        private LocalTime time;
        private String status;
        private String appointmentType;
        private Pet pet;
        private User user;

        public AppointmentBuilder appointmentId(Long appointmentId) { this.appointmentId = appointmentId; return this; }
        public AppointmentBuilder date(LocalDate date) { this.date = date; return this; }
        public AppointmentBuilder time(LocalTime time) { this.time = time; return this; }
        public AppointmentBuilder status(String status) { this.status = status; return this; }
        public AppointmentBuilder appointmentType(String appointmentType) { this.appointmentType = appointmentType; return this; }
        public AppointmentBuilder pet(Pet pet) { this.pet = pet; return this; }
        public AppointmentBuilder user(User user) { this.user = user; return this; }

        public Appointment build() {
            return new Appointment(appointmentId, date, time, status, appointmentType, pet, user);
        }
    }

    // Getters and Setters
    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getAppointmentType() { return appointmentType; }
    public void setAppointmentType(String appointmentType) { this.appointmentType = appointmentType; }
    public Pet getPet() { return pet; }
    public void setPet(Pet pet) { this.pet = pet; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
