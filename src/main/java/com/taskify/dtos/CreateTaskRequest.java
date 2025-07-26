package com.taskify.dtos;

import com.taskify.entities.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record CreateTaskRequest(
    @NotBlank @Size(min = 1, max = 255) String title,
    String description,
    @NotNull LocalDateTime dueDate,
    @NotNull TaskPriority priority) {}
