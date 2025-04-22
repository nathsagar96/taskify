package dev.sagar.taskify.services;

import dev.sagar.taskify.domain.dto.TaskDto;
import dev.sagar.taskify.domain.entities.Task;
import dev.sagar.taskify.domain.entities.TaskList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskService {
  List<Task> listTasks(UUID taskListId);

  Task createTask(UUID taskListId, Task task);

  Optional<Task> getTask(UUID taskListId, UUID taskId);

  Task updateTask(UUID taskListId, UUID taskId, Task task);

  void deleteTask(UUID taskListId, UUID taskId);
}
