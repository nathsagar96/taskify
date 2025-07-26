package com.taskify.mappers;

import com.taskify.dtos.CreateTaskListRequest;
import com.taskify.dtos.TaskListDto;
import com.taskify.dtos.UpdateTaskListRequest;
import com.taskify.entities.TaskList;

public interface TaskListMapper {

  TaskList fromCreateRequest(CreateTaskListRequest request);

  TaskList fromUpdateRequest(UpdateTaskListRequest request);

  TaskListDto toDto(TaskList taskList);
}
