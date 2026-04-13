package com.petadoption.service;

import com.petadoption.dto.AppointmentDTO;
import com.petadoption.exception.ResourceNotFoundException;
import com.petadoption.model.Appointment;
import com.petadoption.model.Pet;
import com.petadoption.model.User;
import com.petadoption.repository.AppointmentRepository;
import com.petadoption.repository.PetRepository;
import com.petadoption.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Appointment bookAppointment(AppointmentDTO appointmentDTO) {
        Pet pet = petRepository.findById(appointmentDTO.getPetId())
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
        User user = userRepository.findById(appointmentDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Appointment appointment = Appointment.builder()
                .date(appointmentDTO.getDate())
                .time(appointmentDTO.getTime())
                .status("PENDING")
                .pet(pet)
                .user(user)
                .build();
        
        com.petadoption.util.AppointmentManager.getInstance().incrementAppointments();
        return appointmentRepository.save(appointment);
    }

    @Override
    public void cancelAppointment(Long appointmentId) {
        if (appointmentRepository.existsById(appointmentId)) {
            appointmentRepository.deleteById(appointmentId);
            com.petadoption.util.AppointmentManager.getInstance().decrementAppointments();
        }
    }

    @Override
    public java.util.List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
}
