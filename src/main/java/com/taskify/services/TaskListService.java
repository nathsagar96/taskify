package com.taskify.services;

import com.taskify.dtos.CreateTaskListRequest;
import com.taskify.dtos.TaskListDto;
import com.taskify.dtos.UpdateTaskListRequest;
import java.util.List;
import java.util.UUID;

public interface TaskListService {
  List<TaskListDto> listTaskLists();

  TaskListDto createTaskList(CreateTaskListRequest request);

  TaskListDto getTaskList(UUID id);

  TaskListDto updateTaskList(UUID taskListId, UpdateTaskListRequest request);

  void deleteTaskList(UUID taskListId);
}
