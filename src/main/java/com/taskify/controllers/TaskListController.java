package com.taskify.controllers;

import com.taskify.dtos.CreateTaskListRequest;
import com.taskify.dtos.TaskListDto;
import com.taskify.dtos.UpdateTaskListRequest;
import com.taskify.services.TaskListService;
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

@Tag(name = "Task Lists")
@RestController
@RequestMapping(path = "/api/v1/task-lists")
public class TaskListController {
  private final TaskListService taskListService;

  public TaskListController(TaskListService taskListService) {
    this.taskListService = taskListService;
  }

  @Operation(summary = "List all task lists")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved list of task lists")
      })
  @GetMapping
  public ResponseEntity<List<TaskListDto>> listTaskLists() {
    return ResponseEntity.status(HttpStatus.OK).body(taskListService.listTaskLists());
  }

  @Operation(summary = "Create a new task list")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Task list created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
      })
  @PostMapping
  public ResponseEntity<TaskListDto> createTaskList(
      @Valid @RequestBody CreateTaskListRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(taskListService.createTaskList(request));
  }

  @Operation(summary = "Get a task list by ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved task list"),
        @ApiResponse(responseCode = "404", description = "Task list not found")
      })
  @GetMapping(path = "/{task_list_id}")
  public ResponseEntity<TaskListDto> getTaskList(@PathVariable("task_list_id") UUID taskListId) {
    return ResponseEntity.status(HttpStatus.OK).body(taskListService.getTaskList(taskListId));
  }

  @Operation(summary = "Update an existing task list")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Task list updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Task list not found")
      })
  @PutMapping(path = "/{task_list_id}")
  public ResponseEntity<TaskListDto> updateTaskList(
      @PathVariable("task_list_id") UUID taskListId,
      @Valid @RequestBody UpdateTaskListRequest request) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(taskListService.updateTaskList(taskListId, request));
  }

  @Operation(summary = "Delete a task list by ID")
  @ApiResponses(
      value = {@ApiResponse(responseCode = "204", description = "Task list deleted successfully")})
  @DeleteMapping(path = "/{task_list_id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> deleteTaskList(@PathVariable("task_list_id") UUID taskListId) {
    taskListService.deleteTaskList(taskListId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
