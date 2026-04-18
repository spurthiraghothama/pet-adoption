package com.petadoption.repository;

import com.petadoption.model.VetAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface VetAppointmentRepository extends JpaRepository<VetAppointment, Long> {
    List<VetAppointment> findByDateAndTime(LocalDate date, LocalTime time);
}
