package com.petadoption.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.petadoption.dto.VetAppointmentDTO;
import com.petadoption.exception.ResourceNotFoundException;
import com.petadoption.model.Status;
import com.petadoption.model.VetAppointment;
import com.petadoption.model.VetAppointmentStatus;
import com.petadoption.model.Pet;
import com.petadoption.model.User;
import com.petadoption.model.Appointment;
import com.petadoption.repository.VetAppointmentRepository;
import com.petadoption.repository.AppointmentRepository;
import com.petadoption.repository.PetRepository;
import com.petadoption.repository.UserRepository;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class VetAppointmentServiceImpl implements VetAppointmentService {

    @Autowired
    private VetAppointmentRepository vetAppointmentRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

@Override
public VetAppointment bookAppointment(VetAppointmentDTO appointmentDTO) {
    com.petadoption.util.AppointmentScheduleValidator.validate(
            appointmentDTO.getDate(),
            appointmentDTO.getTime()
    );

    validateVetSlotAvailability(appointmentDTO);

    Pet pet = petRepository.findById(appointmentDTO.getPetId())
            .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));

    User user = userRepository.findById(appointmentDTO.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    if (!"BREEDER".equalsIgnoreCase(user.getUserType())) {
        throw new RuntimeException("Only breeders can book vet appointments for pets");
    }

    if (!user.getUserId().equals(pet.getRegisteredById()) ||
            !"BREEDER".equalsIgnoreCase(pet.getRegisteredByType())) {
        throw new RuntimeException("Breeders can only book vet appointments for their own pets");
    }

    if (pet.getAvailabilityStatus() != Status.REGISTERED) {
        throw new RuntimeException("Vet appointments can only be booked for pets with REGISTERED status");
    }

    VetAppointment appointment = VetAppointment.builder()
            .date(appointmentDTO.getDate())
            .time(appointmentDTO.getTime())
            .status(VetAppointmentStatus.BOOKED)
            .appointmentType("VET_CHECK")
            .pet(pet)
            .user(user)
            .build();

    com.petadoption.util.AppointmentManager.getInstance().incrementAppointments();
    return vetAppointmentRepository.save(appointment);
}

private void validateVetSlotAvailability(VetAppointmentDTO appointmentDTO) {
    List<Appointment> sameTimeAppointments = appointmentRepository.findAll().stream()
            .filter(this::isActiveAppointment)
            .filter(a -> isSameSlot(a.getDate(), a.getTime(), appointmentDTO.getDate(), appointmentDTO.getTime()))
            .toList();

    boolean hasVetConflictInAppointments = sameTimeAppointments.stream()
            .anyMatch(a -> "VET_CHECK".equalsIgnoreCase(a.getAppointmentType()));

    boolean hasVetConflictInVetAppointments = vetAppointmentRepository.findAll()
            .stream()
            .filter(this::isActiveVetAppointment)
            .filter(a -> isSameSlot(a.getDate(), a.getTime(), appointmentDTO.getDate(), appointmentDTO.getTime()))
            .anyMatch(this::isActiveVetAppointment);

    if (hasVetConflictInAppointments || hasVetConflictInVetAppointments) {
        throw new RuntimeException("Slot not avaliable try again!");
    }
}

private boolean isActiveAppointment(Appointment appointment) {
    String status = appointment.getStatus();
    return status == null || (
            !"REJECTED".equalsIgnoreCase(status) &&
            !"COMPLETED".equalsIgnoreCase(status) &&
            !"EXPIRED".equalsIgnoreCase(status)
    );
}

private boolean isActiveVetAppointment(VetAppointment appointment) {
    VetAppointmentStatus status = appointment.getStatus();
    return status != VetAppointmentStatus.REJECTED &&
            status != VetAppointmentStatus.COMPLETED &&
            status != VetAppointmentStatus.CANCELLED;
}

private boolean isSameSlot(java.time.LocalDate existingDate, LocalTime existingTime,
                           java.time.LocalDate requestedDate, LocalTime requestedTime) {
    if (existingDate == null || existingTime == null || requestedDate == null || requestedTime == null) {
        return false;
    }

    return existingDate.equals(requestedDate) &&
            existingTime.truncatedTo(ChronoUnit.MINUTES)
                    .equals(requestedTime.truncatedTo(ChronoUnit.MINUTES));
}

public VetAppointment confirmAppointment(Long id) {
    VetAppointment app = vetAppointmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

    app.setStatus(VetAppointmentStatus.CONFIRMED);
    return vetAppointmentRepository.save(app);
}

    @Override
    public void cancelAppointment(Long appointmentId) {
        VetAppointment app = vetAppointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        app.setStatus(VetAppointmentStatus.CANCELLED);
        vetAppointmentRepository.save(app);
    }

    @Override
    public java.util.List<VetAppointment> getAllAppointments() {
        return vetAppointmentRepository.findAll();
    }

    @Override
public VetAppointment updateStatus(Long id, String status) {
    VetAppointment app = vetAppointmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

    try {
        app.setStatus(VetAppointmentStatus.valueOf(status.toUpperCase()));
    } catch (Exception e) {
        throw new RuntimeException("Invalid status value");
    }

    return vetAppointmentRepository.save(app);
}

    
    public VetAppointment completeAppointment(Long id) {
    VetAppointment app = vetAppointmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

    app.setStatus(VetAppointmentStatus.COMPLETED);

    Pet pet = app.getPet();
    pet.setVaccinationStatus(true);
    petRepository.save(pet);

    return vetAppointmentRepository.save(app);
}
}
