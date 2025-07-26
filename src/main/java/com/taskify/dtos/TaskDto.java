package com.taskify.dtos;

import com.taskify.entities.TaskPriority;
import com.taskify.entities.TaskStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record TaskDto(
    UUID id,
    String title,
    String description,
    LocalDateTime dueDate,
    TaskPriority priority,
    TaskStatus status) {}
