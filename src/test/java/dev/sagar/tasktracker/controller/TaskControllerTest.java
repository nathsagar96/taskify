package dev.sagar.tasktracker.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import dev.sagar.tasktracker.dto.TaskDTO;
import dev.sagar.tasktracker.dto.TaskResponseDTO;
import dev.sagar.tasktracker.service.TaskService;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

  @Mock
  private TaskService taskService;

  @InjectMocks
  private TaskController taskController;

  private TaskDTO taskDTO;
  private TaskResponseDTO responseDTO;

  @BeforeEach
  void setUp() {
    taskDTO = TaskDTO.builder()
            .title("Test Task")
            .description("Test Description")
            .dueDate(LocalDate.now().plusDays(1))
            .priority(1)
            .completed(false)
            .build();

    responseDTO = TaskResponseDTO.builder()
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
    when(taskService.createTask(anyLong(), any(TaskDTO.class))).thenReturn(responseDTO);

    TaskResponseDTO result = taskController.createTask(1L, taskDTO);

    assertEquals(responseDTO, result);
    verify(taskService, times(1)).createTask(1L, taskDTO);
  }

  @Test
  void getAllTasksByTaskListId_ShouldReturnListOfTasks() {
    List<TaskResponseDTO> tasks = Collections.singletonList(responseDTO);
    when(taskService.getAllTasksByTaskListId(anyLong())).thenReturn(tasks);

    List<TaskResponseDTO> result = taskController.getAllTasksByTaskListId(1L);

    assertEquals(tasks, result);
    verify(taskService, times(1)).getAllTasksByTaskListId(1L);
  }

  @Test
  void getTaskById_ShouldReturnTask() {
    when(taskService.getTaskById(anyLong(), anyLong())).thenReturn(responseDTO);

    TaskResponseDTO result = taskController.getTaskById(1L, 1L);

    assertEquals(responseDTO, result);
    verify(taskService, times(1)).getTaskById(1L, 1L);
  }

  @Test
  void updateTask_ShouldReturnUpdatedTask() {
    when(taskService.updateTask(anyLong(), anyLong(), any(TaskDTO.class))).thenReturn(responseDTO);

    TaskResponseDTO result = taskController.updateTask(1L, 1L, taskDTO);

    assertEquals(responseDTO, result);
    verify(taskService, times(1)).updateTask(1L, 1L, taskDTO);
  }

  @Test
  void completeTask_ShouldReturnCompletedTask() {
    responseDTO.setCompleted(true);
    when(taskService.completeTask(anyLong(), anyLong())).thenReturn(responseDTO);

    TaskResponseDTO result = taskController.completeTask(1L, 1L);

    assertEquals(responseDTO, result);
    assertTrue(result.isCompleted());
    verify(taskService, times(1)).completeTask(1L, 1L);
  }

  @Test
  void deleteTask_ShouldReturnVoid() {
    doNothing().when(taskService).deleteTask(anyLong(), anyLong());

    taskController.deleteTask(1L, 1L);

    verify(taskService, times(1)).deleteTask(1L, 1L);
  }
}