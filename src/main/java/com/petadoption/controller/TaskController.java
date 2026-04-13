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

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskRepository.findAll());
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<Task> acceptTask(@PathVariable Long id, @RequestParam String status) {
        Task task = taskRepository.findById(id).orElseThrow();
        task.setStatus(status);
        return ResponseEntity.ok(taskRepository.save(task));
    }

    @PostMapping("/{id}/details")
    public ResponseEntity<Task> uploadDetails(@PathVariable Long id, @RequestParam String details) {
        Task task = taskRepository.findById(id).orElseThrow();
        task.setSpecifications(details);
        return ResponseEntity.ok(taskRepository.save(task));
    }
}
