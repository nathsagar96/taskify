package dev.sagar.taskify.mappers.impl;

import dev.sagar.taskify.domain.dto.TaskDto;
import dev.sagar.taskify.domain.entities.Task;
import dev.sagar.taskify.mappers.TaskMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskMapperImpl implements TaskMapper {

  @Override
  public Task fromDto(TaskDto dto) {
    return Task.builder()
        .id(dto.id())
        .title(dto.title())
        .description(dto.description())
        .dueDate(dto.dueDate())
        .priority(dto.priority())
        .status(dto.status())
        .build();
  }

  @Override
  public TaskDto toDto(Task task) {
    return new TaskDto(
        task.getId(),
        task.getTitle(),
        task.getDescription(),
        task.getDueDate(),
        task.getPriority(),
        task.getStatus());
  }
}
