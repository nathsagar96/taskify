package com.taskify.mappers.impl;

import com.taskify.dtos.CreateTaskListRequest;
import com.taskify.dtos.TaskListDto;
import com.taskify.dtos.UpdateTaskListRequest;
import com.taskify.entities.TaskList;
import com.taskify.entities.TaskStatus;
import com.taskify.mappers.TaskListMapper;
import com.taskify.mappers.TaskMapper;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TaskListMapperImpl implements TaskListMapper {

  private final TaskMapper taskMapper;

  public TaskListMapperImpl(TaskMapper taskMapper) {
    this.taskMapper = taskMapper;
  }

  @Override
  public TaskList fromCreateRequest(CreateTaskListRequest request) {
    if (request == null) {
      return null;
    }

    TaskList taskList = new TaskList();
    taskList.setTitle(request.title());
    taskList.setDescription(request.description());

    return taskList;
  }

  @Override
  public TaskList fromUpdateRequest(UpdateTaskListRequest request) {
    if (request == null) {
      return null;
    }

    TaskList taskList = new TaskList();
    taskList.setTitle(request.title());
    taskList.setDescription(request.description());
    return taskList;
  }

  @Override
  public TaskListDto toDto(TaskList taskList) {
    if (taskList == null) {
      return null;
    }

    if (taskList.getTasks() == null) {
      return new TaskListDto(
          taskList.getId(), taskList.getTitle(), taskList.getDescription(), 0, 0.0, List.of());
    }

    int count = taskList.getTasks().size();

    double progress =
        taskList.getTasks().stream().filter(task -> task.getStatus() == TaskStatus.CLOSED).count()
            * 100.0
            / Math.max(count, 1);

    return new TaskListDto(
        taskList.getId(),
        taskList.getTitle(),
        taskList.getDescription(),
        count,
        progress,
        taskList.getTasks().stream().map(taskMapper::toDto).toList());
  }
}
