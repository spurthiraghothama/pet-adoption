package com.petadoption.service;

import com.petadoption.dto.AppointmentDTO;
import com.petadoption.model.Appointment;

public interface AppointmentService {
    Appointment bookAppointment(AppointmentDTO appointmentDTO);
    void cancelAppointment(Long appointmentId);
    java.util.List<Appointment> getAllAppointments();
}
