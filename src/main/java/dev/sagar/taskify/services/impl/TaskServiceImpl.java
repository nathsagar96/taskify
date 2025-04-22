package dev.sagar.taskify.services.impl;

import dev.sagar.taskify.domain.entities.Task;
import dev.sagar.taskify.domain.entities.TaskList;
import dev.sagar.taskify.domain.entities.TaskPriority;
import dev.sagar.taskify.domain.entities.TaskStatus;
import dev.sagar.taskify.repositories.TaskListRepository;
import dev.sagar.taskify.repositories.TaskRepository;
import dev.sagar.taskify.services.TaskService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
  private final TaskListRepository taskListRepository;
  private final TaskRepository taskRepository;

  @Override
  public List<Task> listTasks(UUID taskListId) {
    return taskRepository.findByTaskListId(taskListId);
  }

  @Override
  public Task createTask(UUID taskListId, Task task) {
    if (null != task.getId()) {
      throw new IllegalArgumentException("Task already has ID!");
    }

    if (null == task.getTitle() || task.getTitle().isBlank()) {
      throw new IllegalArgumentException("Task must have a title");
    }

    TaskPriority priority = Optional.ofNullable(task.getPriority()).orElse(TaskPriority.MEDIUM);

    TaskList taskList =
        taskListRepository
            .findById(taskListId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Task List ID provided"));

    return taskRepository.save(
        Task.builder()
            .title(task.getTitle())
            .description(task.getDescription())
            .dueDate(task.getDueDate())
            .priority(priority)
            .status(TaskStatus.OPEN)
            .created(LocalDateTime.now())
            .updated(LocalDateTime.now())
            .taskList(taskList)
            .build());
  }

  @Override
  public Optional<Task> getTask(UUID taskListId, UUID taskId) {
    return taskRepository.findByTaskListIdAndId(taskListId, taskId);
  }

  @Override
  public Task updateTask(UUID taskListId, UUID taskId, Task task) {
    if (null == task.getId()) {
      throw new IllegalArgumentException("Task must have ID!");
    }

    if (!Objects.equals(taskId, task.getId())) {
      throw new IllegalArgumentException("Task IDs do not match!");
    }

    if (null == task.getPriority()) {
      throw new IllegalArgumentException("Task must have a valid priority!");
    }

    if (null == task.getStatus()) {
      throw new IllegalArgumentException("Task must have a valid status!");
    }

    Task existingTask =
        taskRepository
            .findByTaskListIdAndId(taskListId, task.getId())
            .orElseThrow(() -> new IllegalStateException("Task not found!"));

    existingTask.setTitle(task.getTitle());
    existingTask.setDescription(task.getDescription());
    existingTask.setDueDate(task.getDueDate());
    existingTask.setPriority(task.getPriority());
    existingTask.setStatus(task.getStatus());
    existingTask.setUpdated(LocalDateTime.now());

    return taskRepository.save(existingTask);
  }

  @Override
  @Transactional
  public void deleteTask(UUID taskListId, UUID taskId) {
    taskRepository.deleteByTaskListIdAndId(taskListId, taskId);
  }
}
