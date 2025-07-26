package com.taskify.services.impl;

import com.taskify.dtos.CreateTaskListRequest;
import com.taskify.dtos.TaskListDto;
import com.taskify.dtos.UpdateTaskListRequest;
import com.taskify.entities.TaskList;
import com.taskify.exceptions.TaskListNotFoundException;
import com.taskify.mappers.TaskListMapper;
import com.taskify.repositories.TaskListRepository;
import com.taskify.services.TaskListService;
import java.util.List;
import java.util.UUID;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TaskListServiceImpl implements TaskListService {

  private final TaskListRepository taskListRepository;
  private final TaskListMapper taskListMapper;

  public TaskListServiceImpl(TaskListRepository taskListRepository, TaskListMapper taskListMapper) {
    this.taskListRepository = taskListRepository;
    this.taskListMapper = taskListMapper;
  }

  @Override
  @Cacheable(value = "taskLists")
  public List<TaskListDto> listTaskLists() {
    return taskListRepository.findAll().stream().map(taskListMapper::toDto).toList();
  }

  @Override
  @Transactional
  public TaskListDto createTaskList(CreateTaskListRequest request) {
    TaskList taskList = taskListMapper.fromCreateRequest(request);
    TaskList createdTaskList = taskListRepository.save(taskList);
    return taskListMapper.toDto(createdTaskList);
  }

  @Override
  @Cacheable(value = "taskLists", key = "#id")
  public TaskListDto getTaskList(UUID id) {
    return taskListRepository
        .findById(id)
        .map(taskListMapper::toDto)
        .orElseThrow(() -> new TaskListNotFoundException("Task List not found with ID: " + id));
  }

  @Override
  @Transactional
  @CachePut(value = "taskLists", key = "#taskListId")
  public TaskListDto updateTaskList(UUID taskListId, UpdateTaskListRequest request) {
    TaskList taskList = taskListMapper.fromUpdateRequest(request);
    TaskList existingTaskList =
        taskListRepository
            .findById(taskListId)
            .orElseThrow(
                () -> new TaskListNotFoundException("Task List not found with ID: " + taskListId));

    existingTaskList.setTitle(taskList.getTitle());
    existingTaskList.setDescription(taskList.getDescription());

    TaskList updatedTaskList = taskListRepository.save(existingTaskList);
    return taskListMapper.toDto(updatedTaskList);
  }

  @Override
  @Transactional
  @CacheEvict(value = "taskLists", key = "#taskListId")
  public void deleteTaskList(UUID taskListId) {
    taskListRepository.deleteById(taskListId);
  }
}
