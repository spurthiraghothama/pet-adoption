package com.petadoption.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentDTO {
    private Long petId;
    private Long userId;
    private LocalDate date;
    private LocalTime time;
    private String appointmentType;

    public Long getPetId() { return petId; }
    public void setPetId(Long petId) { this.petId = petId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }
    public String getAppointmentType() { return appointmentType; }
    public void setAppointmentType(String appointmentType) { this.appointmentType = appointmentType; }
}
