package com.taskify.task.impl;

import com.taskify.common.exceptions.TaskListNotFoundException;
import com.taskify.common.exceptions.TaskNotFoundException;
import com.taskify.task.TaskRepository;
import com.taskify.task.TaskService;
import com.taskify.task.domain.entities.Task;
import com.taskify.task.domain.entities.TaskStatus;
import com.taskify.tasklist.TaskListRepository;
import com.taskify.tasklist.domain.entities.TaskList;
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

    existingTask.setTitle(task.getTitle());
    existingTask.setDescription(task.getDescription());
    existingTask.setDueDate(task.getDueDate());
    existingTask.setPriority(task.getPriority());
    existingTask.setStatus(task.getStatus());

    return taskRepository.save(existingTask);
  }

  @Override
  @Transactional
  public void deleteTask(UUID taskListId, UUID taskId) {
    taskRepository.deleteByTaskListIdAndId(taskListId, taskId);
  }
}
