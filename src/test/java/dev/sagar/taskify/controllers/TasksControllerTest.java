package dev.sagar.taskify.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import dev.sagar.taskify.domain.dto.TaskDto;
import dev.sagar.taskify.domain.entities.Task;
import dev.sagar.taskify.domain.entities.TaskPriority;
import dev.sagar.taskify.domain.entities.TaskStatus;
import dev.sagar.taskify.mappers.TaskMapper;
import dev.sagar.taskify.services.TaskService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TasksControllerTest {

  @Mock private TaskService taskService;

  @Mock private TaskMapper taskMapper;

  @InjectMocks private TasksController tasksController;

  private UUID taskListId;
  private UUID taskId;
  private Task task;
  private TaskDto taskDto;

  @BeforeEach
  void setUp() {
    taskListId = UUID.randomUUID();
    taskId = UUID.randomUUID();

    task =
        Task.builder()
            .id(taskId)
            .title("Test Task")
            .description("Test Description")
            .dueDate(LocalDateTime.now().plusDays(1))
            .priority(TaskPriority.MEDIUM)
            .status(TaskStatus.OPEN)
            .build();

    taskDto =
        new TaskDto(
            taskId,
            "Test Task",
            "Test Description",
            LocalDateTime.now().plusDays(1),
            TaskPriority.MEDIUM,
            TaskStatus.OPEN);
  }

  @Test
  void listTasks_ShouldReturnListOfTasks() {
    // Arrange
    when(taskService.listTasks(taskListId)).thenReturn(List.of(task));
    when(taskMapper.toDto(task)).thenReturn(taskDto);

    // Act
    var result = tasksController.listTasks(taskListId);

    // Assert
    assertEquals(1, result.size());
    assertEquals(taskDto, result.getFirst());
    verify(taskService).listTasks(taskListId);
    verify(taskMapper).toDto(task);
  }

  @Test
  void createTask_ShouldReturnCreatedTask() {
    // Arrange
    when(taskMapper.fromDto(taskDto)).thenReturn(task);
    when(taskService.createTask(taskListId, task)).thenReturn(task);
    when(taskMapper.toDto(task)).thenReturn(taskDto);

    // Act
    var result = tasksController.createTask(taskListId, taskDto);

    // Assert
    assertEquals(taskDto, result);
    verify(taskMapper).fromDto(taskDto);
    verify(taskService).createTask(taskListId, task);
    verify(taskMapper).toDto(task);
  }

  @Test
  void getTask_WhenExists_ShouldReturnTask() {
    // Arrange
    when(taskService.getTask(taskListId, taskId)).thenReturn(Optional.of(task));
    when(taskMapper.toDto(task)).thenReturn(taskDto);

    // Act
    var result = tasksController.getTask(taskListId, taskId);

    // Assert
    assertTrue(result.isPresent());
    assertEquals(taskDto, result.get());
    verify(taskService).getTask(taskListId, taskId);
    verify(taskMapper).toDto(task);
  }

  @Test
  void getTask_WhenNotExists_ShouldReturnEmpty() {
    // Arrange
    when(taskService.getTask(taskListId, taskId)).thenReturn(Optional.empty());

    // Act
    var result = tasksController.getTask(taskListId, taskId);

    // Assert
    assertTrue(result.isEmpty());
    verify(taskService).getTask(taskListId, taskId);
    verifyNoInteractions(taskMapper);
  }

  @Test
  void updateTask_ShouldReturnUpdatedTask() {
    // Arrange
    when(taskMapper.fromDto(taskDto)).thenReturn(task);
    when(taskService.updateTask(taskListId, taskId, task)).thenReturn(task);
    when(taskMapper.toDto(task)).thenReturn(taskDto);

    // Act
    var result = tasksController.updateTask(taskListId, taskId, taskDto);

    // Assert
    assertEquals(taskDto, result);
    verify(taskMapper).fromDto(taskDto);
    verify(taskService).updateTask(taskListId, taskId, task);
    verify(taskMapper).toDto(task);
  }

  @Test
  void deleteTask_ShouldCallService() {
    // Act
    tasksController.deleteTask(taskListId, taskId);

    // Assert
    verify(taskService).deleteTask(taskListId, taskId);
  }
}
