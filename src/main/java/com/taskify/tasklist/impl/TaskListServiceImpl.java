package com.taskify.tasklist.impl;

import com.taskify.common.exceptions.TaskListNotFoundException;
import com.taskify.tasklist.TaskListRepository;
import com.taskify.tasklist.TaskListService;
import com.taskify.tasklist.domain.entities.TaskList;
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
