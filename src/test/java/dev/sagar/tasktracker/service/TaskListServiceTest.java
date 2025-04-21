package dev.sagar.tasktracker.service;

import dev.sagar.tasktracker.dto.TaskListDTO;
import dev.sagar.tasktracker.dto.TaskListResponseDTO;
import dev.sagar.tasktracker.exception.ResourceNotFoundException;
import dev.sagar.tasktracker.model.Task;
import dev.sagar.tasktracker.model.TaskList;
import dev.sagar.tasktracker.repository.TaskListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static dev.sagar.tasktracker.constant.AppConstants.TASK_LIST_NOT_FOUND_WITH_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskListServiceTest {

  @Mock
  private TaskListRepository taskListRepository;

  @InjectMocks
  private TaskListService taskListService;

  private TaskListDTO taskListDTO;
  private TaskList taskList;
  private TaskListResponseDTO responseDTO;

  @BeforeEach
  void setUp() {
    List<Task> tasks = new ArrayList<>();

    taskListDTO = TaskListDTO.builder()
            .title("Test List")
            .description("Test Description")
            .build();

    taskList = TaskList.builder()
            .id(1L)
            .title("Test List")
            .description("Test Description")
            .tasks(tasks)
            .build();

    responseDTO = TaskListResponseDTO.builder()
            .id(1L)
            .title("Test List")
            .description("Test Description")
            .completionPercentage(0.0)
            .build();
  }

  @Test
  void createTaskList_ShouldReturnTaskListResponseDTO() {
    when(taskListRepository.save(any(TaskList.class))).thenReturn(taskList);

    TaskListResponseDTO result = taskListService.createTaskList(taskListDTO);

    assertNotNull(result);
    assertEquals(responseDTO.getId(), result.getId());
    assertEquals(responseDTO.getTitle(), result.getTitle());
    verify(taskListRepository, times(1)).save(any(TaskList.class));
  }

  @Test
  void getAllTaskLists_ShouldReturnListOfTaskLists() {
    when(taskListRepository.findAll()).thenReturn(Collections.singletonList(taskList));

    List<TaskListResponseDTO> result = taskListService.getAllTaskLists();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(responseDTO.getId(), result.getFirst().getId());
    verify(taskListRepository, times(1)).findAll();
  }

  @Test
  void getTaskListById_ShouldReturnTaskList() {
    when(taskListRepository.findById(1L)).thenReturn(Optional.of(taskList));

    TaskListResponseDTO result = taskListService.getTaskListById(1L);

    assertNotNull(result);
    assertEquals(responseDTO.getId(), result.getId());
    verify(taskListRepository, times(1)).findById(1L);
  }

  @Test
  void getTaskListById_ShouldThrowResourceNotFoundException() {
    when(taskListRepository.findById(1L)).thenReturn(Optional.empty());

    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> taskListService.getTaskListById(1L));

    assertEquals(TASK_LIST_NOT_FOUND_WITH_ID + 1L, exception.getMessage());
    verify(taskListRepository, times(1)).findById(1L);
  }

  @Test
  void updateTaskList_ShouldReturnUpdatedTaskList() {
    when(taskListRepository.findById(1L)).thenReturn(Optional.of(taskList));
    when(taskListRepository.save(any(TaskList.class))).thenReturn(taskList);

    TaskListResponseDTO result = taskListService.updateTaskList(1L, taskListDTO);

    assertNotNull(result);
    assertEquals(responseDTO.getId(), result.getId());
    verify(taskListRepository, times(1)).findById(1L);
    verify(taskListRepository, times(1)).save(any(TaskList.class));
  }

  @Test
  void updateTaskList_ShouldThrowResourceNotFoundException() {
    when(taskListRepository.findById(1L)).thenReturn(Optional.empty());

    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> taskListService.updateTaskList(1L, taskListDTO));

    assertEquals(TASK_LIST_NOT_FOUND_WITH_ID + 1L, exception.getMessage());
    verify(taskListRepository, times(1)).findById(1L);
    verify(taskListRepository, never()).save(any());
  }

  @Test
  void deleteTaskList_ShouldDeleteTaskList() {
    when(taskListRepository.existsById(1L)).thenReturn(true);
    doNothing().when(taskListRepository).deleteById(1L);

    taskListService.deleteTaskList(1L);

    verify(taskListRepository, times(1)).existsById(1L);
    verify(taskListRepository, times(1)).deleteById(1L);
  }

  @Test
  void deleteTaskList_ShouldThrowResourceNotFoundException() {
    when(taskListRepository.existsById(1L)).thenReturn(false);

    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> taskListService.deleteTaskList(1L));

    assertEquals(TASK_LIST_NOT_FOUND_WITH_ID + 1L, exception.getMessage());
    verify(taskListRepository, times(1)).existsById(1L);
    verify(taskListRepository, never()).deleteById(any());
  }

  @Test
  void getTaskListEntityById_ShouldReturnTaskList() {
    when(taskListRepository.findById(1L)).thenReturn(Optional.of(taskList));

    TaskList result = taskListService.getTaskListEntityById(1L);

    assertNotNull(result);
    assertEquals(taskList.getId(), result.getId());
    verify(taskListRepository, times(1)).findById(1L);
  }
}