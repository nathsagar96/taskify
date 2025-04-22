package dev.sagar.taskify.mappers;

import dev.sagar.taskify.domain.dto.TaskListDto;
import dev.sagar.taskify.domain.entities.TaskList;

public interface TaskListMapper {
  TaskList fromDto(TaskListDto dto);

  TaskListDto toDto(TaskList taskList);
}
