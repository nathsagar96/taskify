package com.taskify.services;

import com.taskify.dtos.CreateTaskRequest;
import com.taskify.dtos.TaskDto;
import com.taskify.dtos.UpdateTaskRequest;
import java.util.List;
import java.util.UUID;

public interface TaskService {
  List<TaskDto> listTasks(UUID taskListId);

  TaskDto createTask(UUID taskListId, CreateTaskRequest request);

  TaskDto getTask(UUID taskListId, UUID taskId);

  TaskDto updateTask(UUID taskListId, UUID taskId, UpdateTaskRequest request);

  void deleteTask(UUID taskListId, UUID taskId);
}
