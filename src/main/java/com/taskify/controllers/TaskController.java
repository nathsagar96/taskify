package com.taskify.controllers;

import com.taskify.dtos.CreateTaskRequest;
import com.taskify.dtos.TaskDto;
import com.taskify.dtos.UpdateTaskRequest;
import com.taskify.entities.Task;
import com.taskify.mappers.TaskMapper;
import com.taskify.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Tasks")
@RestController
@RequestMapping(path = "/api/v1/task-lists/{task_list_id}/tasks")
public class TaskController {
  private final TaskService taskService;
  private final TaskMapper taskMapper;

  public TaskController(TaskService taskService, TaskMapper taskMapper) {
    this.taskService = taskService;
    this.taskMapper = taskMapper;
  }

  @Operation(summary = "List all tasks for a given task list")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of tasks")
      })
  @GetMapping
  public ResponseEntity<List<TaskDto>> listTasks(@PathVariable("task_list_id") UUID taskListId) {
    List<TaskDto> tasks =
        taskService.listTasks(taskListId).stream().map(taskMapper::toDto).toList();
    return ResponseEntity.ok(tasks);
  }

  @Operation(summary = "Create a new task within a task list")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Task created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Task list not found")
      })
  @PostMapping
  public ResponseEntity<TaskDto> createTask(
      @PathVariable("task_list_id") UUID taskListId,
      @Valid @RequestBody CreateTaskRequest request) {
    Task createdTask = taskService.createTask(taskListId, taskMapper.fromCreateRequest(request));
    return ResponseEntity.status(HttpStatus.CREATED).body(taskMapper.toDto(createdTask));
  }

  @Operation(summary = "Get a task by ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved task"),
        @ApiResponse(responseCode = "404", description = "Task not found")
      })
  @GetMapping("/{task_id}")
  public ResponseEntity<TaskDto> getTask(
      @PathVariable("task_list_id") UUID taskListId, @PathVariable("task_id") UUID taskId) {
    return taskService
        .getTask(taskListId, taskId)
        .map(taskMapper::toDto)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @Operation(summary = "Update an existing task")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Task updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Task or Task list not found")
      })
  @PutMapping(path = "/{task_id}")
  public ResponseEntity<TaskDto> updateTask(
      @PathVariable("task_list_id") UUID taskListId,
      @PathVariable("task_id") UUID id,
      @Valid @RequestBody UpdateTaskRequest request) {
    Task updatedTask =
        taskService.updateTask(taskListId, id, taskMapper.fromUpdateRequest(request));
    return ResponseEntity.ok(taskMapper.toDto(updatedTask));
  }

  @Operation(summary = "Delete a task by ID")
  @ApiResponses(
      value = {@ApiResponse(responseCode = "204", description = "Task deleted successfully")})
  @DeleteMapping(path = "/{task_id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTask(
      @PathVariable("task_list_id") UUID taskListId, @PathVariable("task_id") UUID taskId) {
    taskService.deleteTask(taskListId, taskId);
  }
}
