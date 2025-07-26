package com.taskify.services.impl;

import com.taskify.dtos.CreateTaskRequest;
import com.taskify.dtos.TaskDto;
import com.taskify.dtos.UpdateTaskRequest;
import com.taskify.entities.Task;
import com.taskify.entities.TaskList;
import com.taskify.entities.TaskStatus;
import com.taskify.exceptions.TaskListNotFoundException;
import com.taskify.exceptions.TaskNotFoundException;
import com.taskify.mappers.TaskMapper;
import com.taskify.repositories.TaskListRepository;
import com.taskify.repositories.TaskRepository;
import com.taskify.services.TaskService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {
  private final TaskListRepository taskListRepository;
  private final TaskRepository taskRepository;
  private final TaskMapper taskMapper;

  public TaskServiceImpl(
      TaskListRepository taskListRepository, TaskRepository taskRepository, TaskMapper taskMapper) {
    this.taskListRepository = taskListRepository;
    this.taskRepository = taskRepository;
    this.taskMapper = taskMapper;
  }

  @Override
  @Cacheable(value = "tasks", key = "#taskListId")
  public List<TaskDto> listTasks(UUID taskListId) {
    List<Task> tasks = taskRepository.findByTaskListId(taskListId);
    return tasks.stream().map(taskMapper::toDto).toList();
  }

  @Override
  @Transactional
  public TaskDto createTask(UUID taskListId, CreateTaskRequest request) {
    TaskList taskList =
        taskListRepository
            .findById(taskListId)
            .orElseThrow(
                () ->
                    new TaskListNotFoundException(
                        "Task List not found with ID: "
                            + taskListId
                            + " in Task List: "
                            + taskListId));

    Task task = taskMapper.fromCreateRequest(request);
    task.setStatus(TaskStatus.OPEN);
    task.setTaskList(taskList);

    Task savedTask = taskRepository.save(task);
    return taskMapper.toDto(savedTask);
  }

  @Override
  @Cacheable(value = "tasks", key = "#taskListId + '-' + #taskId")
  public TaskDto getTask(UUID taskListId, UUID taskId) {
    Task task =
        taskRepository
            .findByTaskListIdAndId(taskListId, taskId)
            .orElseThrow(
                () ->
                    new TaskNotFoundException(
                        "Task not found with ID: " + taskId + " in Task List: " + taskListId));
    return taskMapper.toDto(task);
  }

  @Override
  @Transactional
  @CachePut(value = "tasks", key = "#taskListId + '-' + #taskId")
  public TaskDto updateTask(UUID taskListId, UUID taskId, UpdateTaskRequest request) {
    Task existingTask =
        taskRepository
            .findByTaskListIdAndId(taskListId, taskId)
            .orElseThrow(
                () ->
                    new TaskNotFoundException(
                        "Task not found with ID: " + taskId + " in Task List: " + taskListId));

    Task task = taskMapper.fromUpdateRequest(request);

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

    Task updatedTask = taskRepository.save(existingTask);
    return taskMapper.toDto(updatedTask);
  }

  @Override
  @Transactional
  @CacheEvict(value = "tasks", key = "#taskListId + '-' + #taskId")
  public void deleteTask(UUID taskListId, UUID taskId) {
    taskRepository.deleteByTaskListIdAndId(taskListId, taskId);
  }
}
