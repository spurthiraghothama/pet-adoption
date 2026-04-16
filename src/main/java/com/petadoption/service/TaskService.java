package com.petadoption.service;

import com.petadoption.dto.TaskDTO;
import com.petadoption.model.Task;
import com.petadoption.model.TaskStatus;
import java.util.List;
import java.util.Map;

public interface TaskService {
    Task createTask(Map<String, Object> payload);
    Task updateStatus(Long taskId, TaskStatus status);
    List<TaskDTO> getAllTasks();
    List<TaskDTO> getTasksByVolunteer(Long volunteerId);
    Task assignTask(Long taskId, Long volunteerId);
    Task publishTask(Long taskId);
    Task chooseTask(Long taskId, Long volunteerId);
    Task startTask(Long taskId);
    Task markComplete(Long taskId);
    Task reviewTask(Long taskId, boolean approved);
    List<TaskDTO> getPublishedTasks();
}