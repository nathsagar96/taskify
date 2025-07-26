package com.taskify.services.impl;

import com.taskify.entities.TaskList;
import com.taskify.exceptions.TaskListNotFoundException;
import com.taskify.repositories.TaskListRepository;
import com.taskify.services.TaskListService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TaskListServiceImpl implements TaskListService {

  private final TaskListRepository taskListRepository;

  public TaskListServiceImpl(TaskListRepository taskListRepository) {
    this.taskListRepository = taskListRepository;
  }

  @Override
  public List<TaskList> listTaskLists() {
    return taskListRepository.findAll();
  }

  @Override
  @Transactional
  public TaskList createTaskList(TaskList taskList) {
    return taskListRepository.save(taskList);
  }

  @Override
  public Optional<TaskList> getTaskList(UUID id) {
    return taskListRepository.findById(id);
  }

  @Override
  @Transactional
  public TaskList updateTaskList(UUID taskListId, TaskList taskList) {
    TaskList existingTaskList =
        taskListRepository
            .findById(taskListId)
            .orElseThrow(
                () -> new TaskListNotFoundException("Task List not found with ID: " + taskListId));

    existingTaskList.setTitle(taskList.getTitle());
    existingTaskList.setDescription(taskList.getDescription());

    return taskListRepository.save(existingTaskList);
  }

  @Override
  @Transactional
  public void deleteTaskList(UUID taskListId) {
    taskListRepository.deleteById(taskListId);
  }
}
