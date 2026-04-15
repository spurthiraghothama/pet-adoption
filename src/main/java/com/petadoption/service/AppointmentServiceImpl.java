package com.petadoption.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petadoption.dto.AppointmentDTO;
import com.petadoption.exception.ResourceNotFoundException;
import com.petadoption.model.Appointment;
import com.petadoption.model.AppointmentStatus;
import com.petadoption.model.Pet;
import com.petadoption.model.User;
import com.petadoption.repository.AppointmentRepository;
import com.petadoption.repository.PetRepository;
import com.petadoption.repository.UserRepository;

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
            .status(AppointmentStatus.BOOKED)
            .appointmentType("VET_CHECK")
            .pet(pet)
            .user(user)
            .build();

    com.petadoption.util.AppointmentManager.getInstance().incrementAppointments();
    return appointmentRepository.save(appointment);
}

public Appointment confirmAppointment(Long id) {
    Appointment app = appointmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

    app.setStatus(AppointmentStatus.CONFIRMED);
    return appointmentRepository.save(app);
}

    @Override
    public void cancelAppointment(Long appointmentId) {
        Appointment app = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        app.setStatus(AppointmentStatus.CANCELED);
        appointmentRepository.save(app);
    }

    @Override
    public java.util.List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
public Appointment updateStatus(Long id, String status) {
    Appointment app = appointmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

    try {
        app.setStatus(AppointmentStatus.valueOf(status.toUpperCase()));
    } catch (Exception e) {
        throw new RuntimeException("Invalid status value");
    }

    return appointmentRepository.save(app);
}

    
    public Appointment completeAppointment(Long id) {
    Appointment app = appointmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

    app.setStatus(AppointmentStatus.COMPLETED);

    Pet pet = app.getPet();
    pet.setVaccinationStatus(true);
    petRepository.save(pet);

    return appointmentRepository.save(app);
}
}