package dev.sagar.tasktracker.service;

import static dev.sagar.tasktracker.constant.AppConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import dev.sagar.tasktracker.dto.TaskDTO;
import dev.sagar.tasktracker.dto.TaskResponseDTO;
import dev.sagar.tasktracker.exception.ResourceNotFoundException;
import dev.sagar.tasktracker.model.Task;
import dev.sagar.tasktracker.model.TaskList;
import dev.sagar.tasktracker.repository.TaskRepository;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

  @Mock private TaskRepository taskRepository;

  @Mock private TaskListService taskListService;

  @InjectMocks private TaskService taskService;

  private TaskDTO taskDTO;
  private Task task;
  private TaskResponseDTO responseDTO;
  private TaskList taskList;

  @BeforeEach
  void setUp() {
    taskList = TaskList.builder().id(1L).title("Test List").description("Test Description").build();

    taskDTO =
        TaskDTO.builder()
            .title("Test Task")
            .description("Test Description")
            .dueDate(LocalDate.now().plusDays(1))
            .priority(1)
            .completed(false)
            .build();

    task =
        Task.builder()
            .id(1L)
            .title("Test Task")
            .description("Test Description")
            .dueDate(LocalDate.now().plusDays(1))
            .priority(1)
            .completed(false)
            .taskList(taskList)
            .build();

    responseDTO =
        TaskResponseDTO.builder()
            .id(1L)
            .title("Test Task")
            .description("Test Description")
            .dueDate(LocalDate.now().plusDays(1))
            .priority(1)
            .completed(false)
            .taskListId(1L)
            .build();
  }

  @Test
  void createTask_ShouldReturnTaskResponseDTO() {
    when(taskListService.getTaskListEntityById(anyLong())).thenReturn(taskList);
    when(taskRepository.save(any(Task.class))).thenReturn(task);

    TaskResponseDTO result = taskService.createTask(1L, taskDTO);

    assertNotNull(result);
    assertEquals(responseDTO.getId(), result.getId());
    assertEquals(responseDTO.getTitle(), result.getTitle());
    verify(taskListService, times(1)).getTaskListEntityById(1L);
    verify(taskRepository, times(1)).save(any(Task.class));
  }

  @Test
  void getAllTasksByTaskListId_ShouldReturnListOfTasks() {
    when(taskListService.getTaskListEntityById(anyLong())).thenReturn(taskList);
    when(taskRepository.findByTaskListId(anyLong())).thenReturn(Collections.singletonList(task));

    List<TaskResponseDTO> result = taskService.getAllTasksByTaskListId(1L);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(responseDTO.getId(), result.getFirst().getId());
    verify(taskListService, times(1)).getTaskListEntityById(1L);
    verify(taskRepository, times(1)).findByTaskListId(1L);
  }

  @Test
  void getTaskById_ShouldReturnTask() {
    when(taskRepository.findByIdAndTaskListId(anyLong(), anyLong())).thenReturn(Optional.of(task));

    TaskResponseDTO result = taskService.getTaskById(1L, 1L);

    assertNotNull(result);
    assertEquals(responseDTO.getId(), result.getId());
    verify(taskRepository, times(1)).findByIdAndTaskListId(1L, 1L);
  }

  @Test
  void getTaskById_ShouldThrowResourceNotFoundException() {
    when(taskRepository.findByIdAndTaskListId(anyLong(), anyLong())).thenReturn(Optional.empty());

    ResourceNotFoundException exception =
        assertThrows(ResourceNotFoundException.class, () -> taskService.getTaskById(1L, 1L));

    assertEquals(TASK_NOT_FOUND_WITH_ID + 1L + AND_TASK_LIST_ID + 1L, exception.getMessage());
    verify(taskRepository, times(1)).findByIdAndTaskListId(1L, 1L);
  }

  @Test
  void updateTask_ShouldReturnUpdatedTask() {
    when(taskRepository.findByIdAndTaskListId(anyLong(), anyLong())).thenReturn(Optional.of(task));
    when(taskRepository.save(any(Task.class))).thenReturn(task);

    TaskResponseDTO result = taskService.updateTask(1L, 1L, taskDTO);

    assertNotNull(result);
    assertEquals(responseDTO.getId(), result.getId());
    verify(taskRepository, times(1)).findByIdAndTaskListId(1L, 1L);
    verify(taskRepository, times(1)).save(any(Task.class));
  }

  @Test
  void updateTask_ShouldThrowResourceNotFoundException() {
    when(taskRepository.findByIdAndTaskListId(anyLong(), anyLong())).thenReturn(Optional.empty());

    ResourceNotFoundException exception =
        assertThrows(
            ResourceNotFoundException.class, () -> taskService.updateTask(1L, 1L, taskDTO));

    assertEquals(TASK_NOT_FOUND_WITH_ID + 1L + AND_TASK_LIST_ID + 1L, exception.getMessage());
    verify(taskRepository, times(1)).findByIdAndTaskListId(1L, 1L);
    verify(taskRepository, never()).save(any());
  }

  @Test
  void completeTask_ShouldReturnCompletedTask() {
    when(taskRepository.findByIdAndTaskListId(anyLong(), anyLong())).thenReturn(Optional.of(task));
    when(taskRepository.save(any(Task.class))).thenReturn(task);

    TaskResponseDTO result = taskService.completeTask(1L, 1L);

    assertNotNull(result);
    assertTrue(result.isCompleted());
    verify(taskRepository, times(1)).findByIdAndTaskListId(1L, 1L);
    verify(taskRepository, times(1)).save(any(Task.class));
  }

  @Test
  void deleteTask_ShouldDeleteTask() {
    when(taskRepository.findByIdAndTaskListId(anyLong(), anyLong())).thenReturn(Optional.of(task));
    doNothing().when(taskRepository).delete(any(Task.class));

    taskService.deleteTask(1L, 1L);

    verify(taskRepository, times(1)).findByIdAndTaskListId(1L, 1L);
    verify(taskRepository, times(1)).delete(any(Task.class));
  }

  @Test
  void deleteTask_ShouldThrowResourceNotFoundException() {
    when(taskRepository.findByIdAndTaskListId(anyLong(), anyLong())).thenReturn(Optional.empty());

    ResourceNotFoundException exception =
        assertThrows(ResourceNotFoundException.class, () -> taskService.deleteTask(1L, 1L));

    assertEquals(TASK_NOT_FOUND_WITH_ID + 1L + AND_TASK_LIST_ID + 1L, exception.getMessage());
    verify(taskRepository, times(1)).findByIdAndTaskListId(1L, 1L);
    verify(taskRepository, never()).delete(any());
  }
}
