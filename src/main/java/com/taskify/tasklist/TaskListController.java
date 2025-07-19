package com.taskify.tasklist;

import com.taskify.tasklist.domain.dtos.CreateTaskListRequest;
import com.taskify.tasklist.domain.dtos.TaskListDto;
import com.taskify.tasklist.domain.dtos.UpdateTaskListRequest;
import com.taskify.tasklist.domain.entities.TaskList;
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
  private final TaskListMapper taskListMapper;

  public TaskListController(TaskListService taskListService, TaskListMapper taskListMapper) {
    this.taskListService = taskListService;
    this.taskListMapper = taskListMapper;
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
    List<TaskListDto> taskLists =
        taskListService.listTaskLists().stream().map(taskListMapper::toDto).toList();
    return ResponseEntity.ok(taskLists);
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
    TaskList createdTaskList =
        taskListService.createTaskList(taskListMapper.fromCreateRequest(request));
    return ResponseEntity.status(HttpStatus.CREATED).body(taskListMapper.toDto(createdTaskList));
  }

  @Operation(summary = "Get a task list by ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved task list"),
        @ApiResponse(responseCode = "404", description = "Task list not found")
      })
  @GetMapping(path = "/{task_list_id}")
  public ResponseEntity<TaskListDto> getTaskList(@PathVariable("task_list_id") UUID taskListId) {
    return taskListService
        .getTaskList(taskListId)
        .map(taskListMapper::toDto)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
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
    TaskList updatedTaskList =
        taskListService.updateTaskList(taskListId, taskListMapper.fromUpdateRequest(request));
    return ResponseEntity.ok(taskListMapper.toDto(updatedTaskList));
  }

  @Operation(summary = "Delete a task list by ID")
  @ApiResponses(
      value = {@ApiResponse(responseCode = "204", description = "Task list deleted successfully")})
  @DeleteMapping(path = "/{task_list_id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTaskList(@PathVariable("task_list_id") UUID taskListId) {
    taskListService.deleteTaskList(taskListId);
  }
}
