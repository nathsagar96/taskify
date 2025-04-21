package dev.sagar.tasktracker.controller;

import dev.sagar.tasktracker.dto.TaskListDTO;
import dev.sagar.tasktracker.dto.TaskListResponseDTO;
import dev.sagar.tasktracker.service.TaskListService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasklists")
public class TaskListController {

  private final TaskListService taskListService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TaskListResponseDTO createTaskList(@Valid @RequestBody TaskListDTO taskListDTO) {
    return taskListService.createTaskList(taskListDTO);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<TaskListResponseDTO> getAllTaskLists() {
    return taskListService.getAllTaskLists();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public TaskListResponseDTO getTaskListById(@PathVariable Long id) {
    return taskListService.getTaskListById(id);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public TaskListResponseDTO updateTaskList(
      @PathVariable Long id, @Valid @RequestBody TaskListDTO taskListDTO) {
    return taskListService.updateTaskList(id, taskListDTO);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTaskList(@PathVariable Long id) {
    taskListService.deleteTaskList(id);
  }
}
