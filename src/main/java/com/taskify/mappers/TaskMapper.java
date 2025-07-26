package com.taskify.mappers;

import com.taskify.dtos.CreateTaskRequest;
import com.taskify.dtos.TaskDto;
import com.taskify.dtos.UpdateTaskRequest;
import com.taskify.entities.Task;

public interface TaskMapper {

  Task fromCreateRequest(CreateTaskRequest request);

  Task fromUpdateRequest(UpdateTaskRequest request);

  TaskDto toDto(Task task);
}
