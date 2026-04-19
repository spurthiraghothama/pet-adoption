package com.petadoption.service;

import com.petadoption.dto.VetAppointmentDTO;
import com.petadoption.model.VetAppointment;

public interface VetAppointmentService {
    VetAppointment bookAppointment(VetAppointmentDTO appointmentDTO);
    void cancelAppointment(Long appointmentId);
    java.util.List<VetAppointment> getAllAppointments();
    VetAppointment updateStatus(Long id, String status);
    VetAppointment confirmAppointment(Long id);
    VetAppointment completeAppointment(Long id);
}