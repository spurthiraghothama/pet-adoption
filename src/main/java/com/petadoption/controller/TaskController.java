package com.petadoption.controller;

import com.petadoption.dto.TaskDTO;
import com.petadoption.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.petadoption.model.TaskStatus;
import com.petadoption.service.TaskService;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(taskService.createTask(payload));
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<Task> acceptTask(@PathVariable Long id, @RequestParam TaskStatus status) {
        return ResponseEntity.ok(taskService.updateStatus(id, status));
    }

    @GetMapping("/volunteer/{id}")
    public ResponseEntity<List<TaskDTO>> getTasksForVolunteer(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTasksByVolunteer(id));
    }

    @PatchMapping("/{taskId}/assign/{volunteerId}")
    public ResponseEntity<Task> assignTask(
            @PathVariable Long taskId,
            @PathVariable Long volunteerId) {

        return ResponseEntity.ok(taskService.assignTask(taskId, volunteerId));
    }

    @PatchMapping("/{taskId}/publish")
    public ResponseEntity<Task> publishTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.publishTask(taskId));
    }

    @PatchMapping("/{taskId}/choose/{volunteerId}")
    public ResponseEntity<Task> chooseTask(
            @PathVariable Long taskId,
            @PathVariable Long volunteerId) {

        return ResponseEntity.ok(taskService.chooseTask(taskId, volunteerId));
    }

    @PatchMapping("/{taskId}/start")
    public ResponseEntity<Task> startTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.startTask(taskId));
    }

    @PatchMapping("/{taskId}/complete")
    public ResponseEntity<Task> completeTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.markComplete(taskId));
    }

    @PatchMapping("/{taskId}/review")
    public ResponseEntity<Task> reviewTask(
            @PathVariable Long taskId,
            @RequestParam boolean approved) {

        return ResponseEntity.ok(taskService.reviewTask(taskId, approved));
    }

    @GetMapping("/open")
    public ResponseEntity<List<TaskDTO>> getOpenTasks() {
        return ResponseEntity.ok(taskService.getPublishedTasks());
    }

}