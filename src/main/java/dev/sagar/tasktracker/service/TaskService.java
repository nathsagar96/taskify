package dev.sagar.tasktracker.service;

import static dev.sagar.tasktracker.constant.AppConstants.AND_TASK_LIST_ID;
import static dev.sagar.tasktracker.constant.AppConstants.TASK_NOT_FOUND_WITH_ID;

import dev.sagar.tasktracker.dto.TaskDTO;
import dev.sagar.tasktracker.dto.TaskResponseDTO;
import dev.sagar.tasktracker.exception.ResourceNotFoundException;
import dev.sagar.tasktracker.model.Task;
import dev.sagar.tasktracker.model.TaskList;
import dev.sagar.tasktracker.repository.TaskRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

  private final TaskRepository taskRepository;
  private final TaskListService taskListService;

  public TaskResponseDTO createTask(Long taskListId, TaskDTO taskDTO) {
    TaskList taskList = taskListService.getTaskListEntityById(taskListId);

    Task task =
        Task.builder()
            .title(taskDTO.getTitle())
            .description(taskDTO.getDescription())
            .dueDate(taskDTO.getDueDate())
            .priority(taskDTO.getPriority())
            .taskList(taskList)
            .build();

    Task savedTask = taskRepository.save(task);
    return convertToResponseDTO(savedTask);
  }

  public List<TaskResponseDTO> getAllTasksByTaskListId(Long taskListId) {
    taskListService.getTaskListEntityById(taskListId);

    return taskRepository.findByTaskListId(taskListId).stream()
        .map(this::convertToResponseDTO)
        .toList();
  }

  public TaskResponseDTO getTaskById(Long taskListId, Long taskId) {
    Task task =
        taskRepository
            .findByIdAndTaskListId(taskId, taskListId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        TASK_NOT_FOUND_WITH_ID + taskId + AND_TASK_LIST_ID + taskListId));
    return convertToResponseDTO(task);
  }

  public TaskResponseDTO updateTask(Long taskListId, Long taskId, TaskDTO taskDTO) {
    Task task =
        taskRepository
            .findByIdAndTaskListId(taskId, taskListId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        TASK_NOT_FOUND_WITH_ID + taskId + AND_TASK_LIST_ID + taskListId));

    task.setTitle(taskDTO.getTitle());
    task.setDescription(taskDTO.getDescription());
    task.setDueDate(taskDTO.getDueDate());
    task.setPriority(taskDTO.getPriority());

    if (taskDTO.getCompleted() != null) {
      task.setCompleted(taskDTO.getCompleted());
    }

    Task updatedTask = taskRepository.save(task);
    return convertToResponseDTO(updatedTask);
  }

  public TaskResponseDTO completeTask(Long taskListId, Long taskId) {
    Task task =
        taskRepository
            .findByIdAndTaskListId(taskId, taskListId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        TASK_NOT_FOUND_WITH_ID + taskId + AND_TASK_LIST_ID + taskListId));

    task.setCompleted(true);
    Task updatedTask = taskRepository.save(task);
    return convertToResponseDTO(updatedTask);
  }

  public void deleteTask(Long taskListId, Long taskId) {
    Task task =
        taskRepository
            .findByIdAndTaskListId(taskId, taskListId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        TASK_NOT_FOUND_WITH_ID + taskId + AND_TASK_LIST_ID + taskListId));

    taskRepository.delete(task);
  }

  private TaskResponseDTO convertToResponseDTO(Task task) {

    return TaskResponseDTO.builder()
        .id(task.getId())
        .title(task.getTitle())
        .description(task.getDescription())
        .dueDate(task.getDueDate())
        .priority(task.getPriority())
        .completed(task.isCompleted())
        .taskListId(task.getTaskList().getId())
        .build();
  }
}
