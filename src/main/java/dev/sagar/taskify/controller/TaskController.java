package dev.sagar.taskify.controller;

import dev.sagar.taskify.dto.TaskDTO;
import dev.sagar.taskify.dto.TaskResponseDTO;
import dev.sagar.taskify.service.TaskService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasklists/{taskListId}/tasks")
public class TaskController {

  private final TaskService taskService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TaskResponseDTO createTask(
      @PathVariable Long taskListId, @RequestBody @Valid TaskDTO taskDTO) {
    return taskService.createTask(taskListId, taskDTO);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<TaskResponseDTO> getAllTasksByTaskListId(@PathVariable Long taskListId) {
    return taskService.getAllTasksByTaskListId(taskListId);
  }

  @GetMapping("/{taskId}")
  @ResponseStatus(HttpStatus.OK)
  public TaskResponseDTO getTaskById(@PathVariable Long taskListId, @PathVariable Long taskId) {
    return taskService.getTaskById(taskListId, taskId);
  }

  @PutMapping("/{taskId}")
  @ResponseStatus(HttpStatus.OK)
  public TaskResponseDTO updateTask(
      @PathVariable Long taskListId,
      @PathVariable Long taskId,
      @Valid @RequestBody TaskDTO taskDTO) {
    return taskService.updateTask(taskListId, taskId, taskDTO);
  }

  @PatchMapping("/{taskId}/complete")
  @ResponseStatus(HttpStatus.OK)
  public TaskResponseDTO completeTask(@PathVariable Long taskListId, @PathVariable Long taskId) {
    return taskService.completeTask(taskListId, taskId);
  }

  @DeleteMapping("/{taskId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTask(@PathVariable Long taskListId, @PathVariable Long taskId) {
    taskService.deleteTask(taskListId, taskId);
  }
}
