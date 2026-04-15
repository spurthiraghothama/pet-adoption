package com.petadoption.service;

import com.petadoption.dto.TaskDTO;
import com.petadoption.model.Task;
import com.petadoption.model.TaskStatus;
import com.petadoption.model.Volunteer;
import com.petadoption.repository.TaskRepository;
import com.petadoption.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import com.petadoption.model.User;
import com.petadoption.dto.TaskDTO;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Task createTask(Map<String, Object> payload) {
        Task task = new Task();
        task.setDescription((String) payload.get("description"));
        task.setSpecifications((String) payload.get("specifications"));
        task.setStatus(TaskStatus.DRAFTED);

        if (payload.containsKey("volunteerId") && payload.get("volunteerId") != null) {
            Long vId = Long.valueOf(payload.get("volunteerId").toString());
            userRepository.findById(vId).ifPresent(v -> {
                if (v instanceof Volunteer) {
                    task.setVolunteer((Volunteer) v);
                }
            });
        }

        return taskRepository.save(task);
    }

    @Override
    public Task updateStatus(Long taskId, TaskStatus status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found: " + taskId));
        task.setStatus(status);
        return taskRepository.save(task);
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(task -> new TaskDTO(
                        task.getTaskId(),
                        task.getDescription(),
                        task.getSpecifications(),
                        task.getStatus(),
                        task.getVolunteer() != null ? task.getVolunteer().getUserId() : null
                ))
                .toList();
    }

    @Override
    public List<TaskDTO> getTasksByVolunteer(Long volunteerId) {
        return taskRepository.findByVolunteer_UserId(volunteerId)
                .stream()
                .map(task -> new TaskDTO(
                        task.getTaskId(),
                        task.getDescription(),
                        task.getSpecifications(),
                        task.getStatus(),
                        volunteerId
                ))
                .toList();
    }

    @Override
    public Task assignTask(Long taskId, Long volunteerId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        var user = userRepository.findById(volunteerId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!(user instanceof Volunteer)) {
            throw new RuntimeException("User is not a volunteer");
        }

        Volunteer volunteer = (Volunteer) user;
        task.setVolunteer(volunteer);
        task.setStatus(TaskStatus.ASSIGNED);

        return taskRepository.save(task);
    }

    @Override
    public Task publishTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(TaskStatus.PUBLISHED);
        return taskRepository.save(task);
    }

    @Override
    public Task chooseTask(Long taskId, Long volunteerId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (task.getStatus() != TaskStatus.PUBLISHED) {
            throw new RuntimeException("Task is not available");
        }

        User user = userRepository.findById(volunteerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!(user instanceof Volunteer)) {
            throw new RuntimeException("Not a volunteer");
        }

        task.setVolunteer((Volunteer) user);
        task.setStatus(TaskStatus.ASSIGNED);

        return taskRepository.save(task);
    }

    @Override
    public Task startTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(TaskStatus.IN_PROGRESS);
        return taskRepository.save(task);
    }


    @Override
    public Task markComplete(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(TaskStatus.MONITORED);
        return taskRepository.save(task);
    }

    @Override
    public Task reviewTask(Long taskId, boolean approved) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (approved) {
            task.setStatus(TaskStatus.COMPLETED);
        } else {
            task.setStatus(TaskStatus.IN_PROGRESS);
        }

        return taskRepository.save(task);
    }

    @Override
    public List<TaskDTO> getPublishedTasks() {
        return taskRepository.findByStatus(TaskStatus.PUBLISHED)
                .stream()
                .map(task -> new TaskDTO(
                        task.getTaskId(),
                        task.getDescription(),
                        task.getSpecifications(),
                        task.getStatus(),
                        task.getVolunteer() != null ? task.getVolunteer().getUserId() : null
                ))
                .toList();
    }

}

