package dev.sagar.taskify.service;

import static dev.sagar.taskify.constant.AppConstants.TASK_LIST_NOT_FOUND_WITH_ID;

import dev.sagar.taskify.dto.TaskListDTO;
import dev.sagar.taskify.dto.TaskListResponseDTO;
import dev.sagar.taskify.exception.ResourceNotFoundException;
import dev.sagar.taskify.model.TaskList;
import dev.sagar.taskify.repository.TaskListRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskListService {

  private final TaskListRepository taskListRepository;

  public TaskListResponseDTO createTaskList(TaskListDTO taskListDTO) {
    TaskList taskList =
        TaskList.builder()
            .title(taskListDTO.getTitle())
            .description(taskListDTO.getDescription())
            .build();

    TaskList savedTaskList = taskListRepository.save(taskList);
    return convertToResponseDTO(savedTaskList);
  }

  public List<TaskListResponseDTO> getAllTaskLists() {
    return taskListRepository.findAll().stream().map(this::convertToResponseDTO).toList();
  }

  public TaskListResponseDTO getTaskListById(Long id) {
    TaskList taskList =
        taskListRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(TASK_LIST_NOT_FOUND_WITH_ID + id));
    return convertToResponseDTO(taskList);
  }

  public TaskListResponseDTO updateTaskList(Long id, TaskListDTO taskListDTO) {
    TaskList taskList =
        taskListRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(TASK_LIST_NOT_FOUND_WITH_ID + id));

    taskList.setTitle(taskListDTO.getTitle());
    taskList.setDescription(taskListDTO.getDescription());

    TaskList updatedTaskList = taskListRepository.save(taskList);
    return convertToResponseDTO(updatedTaskList);
  }

  public void deleteTaskList(Long id) {
    if (!taskListRepository.existsById(id)) {
      throw new ResourceNotFoundException(TASK_LIST_NOT_FOUND_WITH_ID + id);
    }
    taskListRepository.deleteById(id);
  }

  public TaskList getTaskListEntityById(Long id) {
    return taskListRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(TASK_LIST_NOT_FOUND_WITH_ID + id));
  }

  private TaskListResponseDTO convertToResponseDTO(TaskList taskList) {
    return TaskListResponseDTO.builder()
        .id(taskList.getId())
        .title(taskList.getTitle())
        .description(taskList.getDescription())
        .completionPercentage(taskList.getCompletionPercentage())
        .build();
  }
}
