package com.taskify.dtos;

import com.taskify.entities.TaskPriority;
import com.taskify.entities.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record UpdateTaskRequest(
    @NotBlank @Size(min = 1, max = 255) String title,
    String description,
    LocalDateTime dueDate,
    TaskPriority priority,
    TaskStatus status) {}
