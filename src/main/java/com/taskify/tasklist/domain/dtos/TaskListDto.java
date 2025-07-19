package com.taskify.tasklist.domain.dtos;

import com.taskify.task.domain.dtos.TaskDto;
import java.util.List;
import java.util.UUID;

public record TaskListDto(
    UUID id,
    String title,
    String description,
    Integer count,
    Double progress,
    List<TaskDto> tasks) {}
