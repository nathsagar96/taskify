package dev.sagar.taskify.controllers;

import dev.sagar.taskify.domain.dto.TaskDto;
import dev.sagar.taskify.domain.entities.Task;
import dev.sagar.taskify.mappers.TaskMapper;
import dev.sagar.taskify.services.TaskService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/task-lists/{task_list_id}/tasks")
public class TasksController {
  private final TaskService taskService;
  private final TaskMapper taskMapper;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<TaskDto> listTasks(@PathVariable("task_list_id") UUID taskListId) {
    return taskService.listTasks(taskListId).stream().map(taskMapper::toDto).toList();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TaskDto createTask(
      @PathVariable("task_list_id") UUID taskListId, @RequestBody TaskDto taskDto) {
    Task createdTask = taskService.createTask(taskListId, taskMapper.fromDto(taskDto));
    return taskMapper.toDto(createdTask);
  }

  @GetMapping("/{task_id}")
  @ResponseStatus(HttpStatus.OK)
  public Optional<TaskDto> getTask(
      @PathVariable("task_list_id") UUID taskListId, @PathVariable("task_id") UUID taskId) {
    return taskService.getTask(taskListId, taskId).map(taskMapper::toDto);
  }

  @PutMapping(path = "/{task_id}")
  @ResponseStatus(HttpStatus.OK)
  public TaskDto updateTask(
      @PathVariable("task_list_id") UUID taskListId,
      @PathVariable("task_id") UUID id,
      @RequestBody TaskDto taskDto) {
    Task updatedTask = taskService.updateTask(taskListId, id, taskMapper.fromDto(taskDto));
    return taskMapper.toDto(updatedTask);
  }

  @DeleteMapping(path = "/{task_id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTask(
      @PathVariable("task_list_id") UUID taskListId, @PathVariable("task_id") UUID taskId) {
    taskService.deleteTask(taskListId, taskId);
  }
}
