package com.petadoption.controller;

import com.petadoption.model.Task;
import com.petadoption.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private com.petadoption.repository.UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskRepository.findAll());
    }

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody java.util.Map<String, Object> payload) {
        Task task = new Task();
        task.setDescription((String) payload.get("description"));
        task.setSpecifications((String) payload.get("specifications"));
        task.setStatus("PENDING");
        
        if (payload.containsKey("volunteerId") && payload.get("volunteerId") != null) {
            Long vId = Long.valueOf(payload.get("volunteerId").toString());
            userRepository.findById(vId).ifPresent(v -> {
                if (v instanceof com.petadoption.model.Volunteer) {
                    task.setVolunteer((com.petadoption.model.Volunteer) v);
                }
            });
        }
        
        return ResponseEntity.ok(taskRepository.save(task));
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<Task> acceptTask(@PathVariable Long id, @RequestParam String status) {
        Task task = taskRepository.findById(id).orElseThrow();
        task.setStatus(status);
        return ResponseEntity.ok(taskRepository.save(task));
    }
}
