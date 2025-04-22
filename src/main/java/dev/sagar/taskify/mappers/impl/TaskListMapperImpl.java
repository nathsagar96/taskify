package dev.sagar.taskify.mappers.impl;

import dev.sagar.taskify.domain.dto.TaskListDto;
import dev.sagar.taskify.domain.entities.Task;
import dev.sagar.taskify.domain.entities.TaskList;
import dev.sagar.taskify.domain.entities.TaskStatus;
import dev.sagar.taskify.mappers.TaskListMapper;
import dev.sagar.taskify.mappers.TaskMapper;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskListMapperImpl implements TaskListMapper {
  private final TaskMapper taskMapper;

  @Override
  public TaskList fromDto(TaskListDto dto) {
    return TaskList.builder()
        .id(dto.id())
        .title(dto.title())
        .description(dto.description())
        .tasks(
            Optional.ofNullable(dto.tasks())
                .map(tasks -> tasks.stream().map(taskMapper::fromDto).toList())
                .orElse(null))
        .build();
  }

  @Override
  public TaskListDto toDto(TaskList taskList) {
    final List<Task> tasks = taskList.getTasks();
    return new TaskListDto(
        taskList.getId(),
        taskList.getTitle(),
        taskList.getDescription(),
        Optional.ofNullable(tasks).map(List::size).orElse(0),
        calculateTaskListProgress(tasks),
        Optional.ofNullable(tasks)
            .map(t -> t.stream().map(taskMapper::toDto).toList())
            .orElse(null));
  }

  private Double calculateTaskListProgress(List<Task> tasks) {
    if (null == tasks) {
      return null;
    }
    long closedTaskCount =
        tasks.stream().filter(task -> TaskStatus.CLOSED == task.getStatus()).count();
    return (double) closedTaskCount / tasks.size();
  }
}
