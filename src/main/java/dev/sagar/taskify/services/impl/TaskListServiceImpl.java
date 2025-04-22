package dev.sagar.taskify.services.impl;

import dev.sagar.taskify.domain.entities.TaskList;
import dev.sagar.taskify.repositories.TaskListRepository;
import dev.sagar.taskify.services.TaskListService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskListServiceImpl implements TaskListService {

  private final TaskListRepository taskListRepository;

  @Override
  public List<TaskList> listTaskLists() {
    return taskListRepository.findAll();
  }

  @Override
  public TaskList createTaskList(TaskList taskList) {
    if (null != taskList.getId()) {
      throw new IllegalArgumentException("Task list already has an ID!");
    }

    if (null == taskList.getTitle() || taskList.getTitle().isBlank()) {
      throw new IllegalArgumentException("Task list title must be present!");
    }

    return taskListRepository.save(
        TaskList.builder()
            .title(taskList.getTitle())
            .description(taskList.getDescription())
            .created(LocalDateTime.now())
            .updated(LocalDateTime.now())
            .build());
  }

  @Override
  public Optional<TaskList> getTaskList(UUID id) {
    return taskListRepository.findById(id);
  }

  @Override
  public TaskList updateTaskList(UUID taskListId, TaskList taskList) {
    if (null == taskList.getId()) {
      throw new IllegalArgumentException("Task list must have an ID!");
    }

    if (!Objects.equals(taskList.getId().toString(), taskListId.toString())) {
      throw new IllegalArgumentException(
          "Attempting to change task list ID, this is not permitted!");
    }

    TaskList existingTaskList =
        taskListRepository
            .findById(taskListId)
            .orElseThrow(() -> new IllegalStateException("Task list not found!"));

    existingTaskList.setTitle(taskList.getTitle());
    existingTaskList.setDescription(taskList.getDescription());
    existingTaskList.setUpdated(LocalDateTime.now());

    return taskListRepository.save(existingTaskList);
  }

  @Override
  public void deleteTaskList(UUID taskListId) {
    taskListRepository.deleteById(taskListId);
  }
}
