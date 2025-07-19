package com.taskify.tasklist.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTaskListRequest(
    @NotBlank @Size(min = 1, max = 255) String title, String description) {}
