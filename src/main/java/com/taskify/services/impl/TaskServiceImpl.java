package com.taskify.services.impl;

import com.taskify.entities.Task;
import com.taskify.entities.TaskList;
import com.taskify.entities.TaskStatus;
import com.taskify.exceptions.TaskListNotFoundException;
import com.taskify.exceptions.TaskNotFoundException;
import com.taskify.repositories.TaskListRepository;
import com.taskify.repositories.TaskRepository;
import com.taskify.services.TaskService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {
  private final TaskListRepository taskListRepository;
  private final TaskRepository taskRepository;

  public TaskServiceImpl(TaskListRepository taskListRepository, TaskRepository taskRepository) {
    this.taskListRepository = taskListRepository;
    this.taskRepository = taskRepository;
  }

  @Override
  public List<Task> listTasks(UUID taskListId) {
    return taskRepository.findByTaskListId(taskListId);
  }

  @Override
  @Transactional
  public Task createTask(UUID taskListId, Task task) {
    TaskList taskList =
        taskListRepository
            .findById(taskListId)
            .orElseThrow(
                () -> new TaskListNotFoundException("Task List not found with ID: " + taskListId));

    task.setStatus(TaskStatus.OPEN);
    task.setTaskList(taskList);

    return taskRepository.save(task);
  }

  @Override
  public Optional<Task> getTask(UUID taskListId, UUID taskId) {
    return taskRepository.findByTaskListIdAndId(taskListId, taskId);
  }

  @Override
  @Transactional
  public Task updateTask(UUID taskListId, UUID taskId, Task task) {
    Task existingTask =
        taskRepository
            .findByTaskListIdAndId(taskListId, taskId)
            .orElseThrow(
                () ->
                    new TaskNotFoundException(
                        "Task not found with ID: " + taskId + " in Task List: " + taskListId));

    if (task.getTitle() != null) {
      existingTask.setTitle(task.getTitle());
    }

    if (task.getDescription() != null) {
      existingTask.setDescription(task.getDescription());
    }

    if (task.getDueDate() != null) {
      if (task.getDueDate().isBefore(LocalDateTime.now())) {
        throw new IllegalArgumentException("Due date cannot be in the past");
      }
      existingTask.setDueDate(task.getDueDate());
    }

    if (task.getPriority() != null) {
      existingTask.setPriority(task.getPriority());
    }

    if (task.getStatus() != null) {
      existingTask.setStatus(task.getStatus());
    }

    return taskRepository.save(existingTask);
  }

  @Override
  @Transactional
  public void deleteTask(UUID taskListId, UUID taskId) {
    taskRepository.deleteByTaskListIdAndId(taskListId, taskId);
  }
}
