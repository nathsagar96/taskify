package dev.sagar.taskify.mappers;

import dev.sagar.taskify.domain.dto.TaskDto;
import dev.sagar.taskify.domain.entities.Task;

public interface TaskMapper {
  Task fromDto(TaskDto dto);

  TaskDto toDto(Task task);
}
