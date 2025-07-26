package com.taskify.mappers.impl;

import com.taskify.dtos.CreateTaskRequest;
import com.taskify.dtos.TaskDto;
import com.taskify.dtos.UpdateTaskRequest;
import com.taskify.entities.Task;
import com.taskify.mappers.TaskMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskMapperImpl implements TaskMapper {

  @Override
  public Task fromCreateRequest(CreateTaskRequest request) {
    if (request == null) {
      return null;
    }

    Task task = new Task();
    task.setTitle(request.title());
    task.setDescription(request.description());
    task.setDueDate(request.dueDate());
    task.setPriority(request.priority());

    return task;
  }

  @Override
  public Task fromUpdateRequest(UpdateTaskRequest request) {
    if (request == null) {
      return null;
    }

    Task task = new Task();
    task.setTitle(request.title());
    task.setDescription(request.description());
    task.setDueDate(request.dueDate());
    task.setPriority(request.priority());
    task.setStatus(request.status());

    return task;
  }

  @Override
  public TaskDto toDto(Task task) {
    if (task == null) {
      return null;
    }

    return new TaskDto(
        task.getId(),
        task.getTitle(),
        task.getDescription(),
        task.getDueDate(),
        task.getPriority(),
        task.getStatus());
  }
}
