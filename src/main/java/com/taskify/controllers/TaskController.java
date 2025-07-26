package com.taskify.controllers;

import com.taskify.dtos.CreateTaskRequest;
import com.taskify.dtos.TaskDto;
import com.taskify.dtos.UpdateTaskRequest;
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

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @Operation(summary = "List all tasks for a given task list")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of tasks")
      })
  @GetMapping
  public ResponseEntity<List<TaskDto>> listTasks(@PathVariable("task_list_id") UUID taskListId) {
    return ResponseEntity.status(HttpStatus.OK).body(taskService.listTasks(taskListId));
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
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(taskService.createTask(taskListId, request));
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
    return ResponseEntity.status(HttpStatus.OK).body(taskService.getTask(taskListId, taskId));
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
    return ResponseEntity.status(HttpStatus.OK)
        .body(taskService.updateTask(taskListId, id, request));
  }

  @Operation(summary = "Delete a task by ID")
  @ApiResponses(
      value = {@ApiResponse(responseCode = "204", description = "Task deleted successfully")})
  @DeleteMapping(path = "/{task_id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> deleteTask(
      @PathVariable("task_list_id") UUID taskListId, @PathVariable("task_id") UUID taskId) {
    taskService.deleteTask(taskListId, taskId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
