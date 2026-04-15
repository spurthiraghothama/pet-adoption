package com.petadoption.repository;

import com.petadoption.model.Task;
import com.petadoption.model.TaskStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByVolunteer_UserId(Long volunteerId);
    List<Task> findByStatus(TaskStatus status);
}
