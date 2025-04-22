package dev.sagar.taskify.controllers;

import dev.sagar.taskify.domain.dto.TaskListDto;
import dev.sagar.taskify.domain.entities.TaskList;
import dev.sagar.taskify.mappers.TaskListMapper;
import dev.sagar.taskify.services.TaskListService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/task-lists")
public class TaskListController {
  private final TaskListService taskListService;
  private final TaskListMapper taskListMapper;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<TaskListDto> listTaskLists() {
    return taskListService.listTaskLists().stream().map(taskListMapper::toDto).toList();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TaskListDto createTaskList(@RequestBody TaskListDto taskListDto) {
    TaskList createdTaskList = taskListService.createTaskList(taskListMapper.fromDto(taskListDto));
    return taskListMapper.toDto(createdTaskList);
  }

  @GetMapping(path = "/{task_list_id}")
  @ResponseStatus(HttpStatus.OK)
  public Optional<TaskListDto> getTaskList(@PathVariable("task_list_id") UUID taskListId) {
    return taskListService.getTaskList(taskListId).map(taskListMapper::toDto);
  }

  @PutMapping(path = "/{task_list_id}")
  @ResponseStatus(HttpStatus.OK)
  public TaskListDto updateTaskList(
      @PathVariable("task_list_id") UUID taskListId, @RequestBody TaskListDto taskListDto) {
    TaskList updatedTaskList =
        taskListService.updateTaskList(taskListId, taskListMapper.fromDto(taskListDto));
    return taskListMapper.toDto(updatedTaskList);
  }

  @DeleteMapping(path = "/{task_list_id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTaskList(@PathVariable("task_list_id") UUID taskListId) {
    taskListService.deleteTaskList(taskListId);
  }
}
