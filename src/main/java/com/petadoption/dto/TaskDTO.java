package com.petadoption.dto;

import com.petadoption.model.TaskStatus;

public class TaskDTO {

    private Long taskId;
    private String description;
    private String specifications;
    private TaskStatus status;
    private Long volunteerId;
    private String volunteerName;

    public TaskDTO(Long taskId, String description, String specifications,
                   TaskStatus status, Long volunteerId, String volunteerName) {
        this.taskId = taskId;
        this.description = description;
        this.specifications = specifications;
        this.status = status;
        this.volunteerId = volunteerId;
        this.volunteerName = volunteerName;
    }

    // getters only (no setters needed)
    public Long getTaskId() { return taskId; }
    public String getDescription() { return description; }
    public String getSpecifications() { return specifications; }
    public TaskStatus getStatus() { return status; }
    public Long getVolunteerId() { return volunteerId; }
    public String getVolunteerName() { return volunteerName; }
}