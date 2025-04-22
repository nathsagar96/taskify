package dev.sagar.taskify.mappers.impl;

import static org.junit.jupiter.api.Assertions.*;

import dev.sagar.taskify.domain.dto.TaskDto;
import dev.sagar.taskify.domain.entities.Task;
import dev.sagar.taskify.domain.entities.TaskPriority;
import dev.sagar.taskify.domain.entities.TaskStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskMapperImplTest {

  @InjectMocks private TaskMapperImpl taskMapper;

  private Task task;
  private TaskDto taskDto;
  private LocalDateTime dueDate;

  @BeforeEach
  void setUp() {
    dueDate = LocalDateTime.now().plusDays(1);

    task =
        Task.builder()
            .id(UUID.randomUUID())
            .title("Test Task")
            .description("Test Description")
            .dueDate(dueDate)
            .priority(TaskPriority.HIGH)
            .status(TaskStatus.OPEN)
            .build();

    taskDto =
        new TaskDto(
            task.getId(),
            "Test Task",
            "Test Description",
            dueDate,
            TaskPriority.HIGH,
            TaskStatus.OPEN);
  }

  @Test
  void fromDto_ShouldMapTaskDtoToTask() {
    // Act
    Task result = taskMapper.fromDto(taskDto);

    // Assert
    assertNotNull(result);
    assertEquals(taskDto.id(), result.getId());
    assertEquals(taskDto.title(), result.getTitle());
    assertEquals(taskDto.description(), result.getDescription());
    assertEquals(taskDto.dueDate(), result.getDueDate());
    assertEquals(taskDto.priority(), result.getPriority());
    assertEquals(taskDto.status(), result.getStatus());
  }

  @Test
  void fromDto_WithNullValues_ShouldMapCorrectly() {
    // Arrange
    TaskDto nullDto = new TaskDto(null, null, null, null, null, null);

    // Act
    Task result = taskMapper.fromDto(nullDto);

    // Assert
    assertNotNull(result);
    assertNull(result.getId());
    assertNull(result.getTitle());
    assertNull(result.getDescription());
    assertNull(result.getDueDate());
    assertNull(result.getPriority());
    assertNull(result.getStatus());
  }

  @Test
  void toDto_ShouldMapTaskToTaskDto() {
    // Act
    TaskDto result = taskMapper.toDto(task);

    // Assert
    assertNotNull(result);
    assertEquals(task.getId(), result.id());
    assertEquals(task.getTitle(), result.title());
    assertEquals(task.getDescription(), result.description());
    assertEquals(task.getDueDate(), result.dueDate());
    assertEquals(task.getPriority(), result.priority());
    assertEquals(task.getStatus(), result.status());
  }

  @Test
  void toDto_WithNullValues_ShouldMapCorrectly() {
    // Arrange
    Task nullTask = Task.builder().build();

    // Act
    TaskDto result = taskMapper.toDto(nullTask);

    // Assert
    assertNotNull(result);
    assertNull(result.id());
    assertNull(result.title());
    assertNull(result.description());
    assertNull(result.dueDate());
    assertNull(result.priority());
    assertNull(result.status());
  }
}
