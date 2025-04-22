package dev.sagar.taskify.mappers.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import dev.sagar.taskify.domain.dto.TaskDto;
import dev.sagar.taskify.domain.dto.TaskListDto;
import dev.sagar.taskify.domain.entities.Task;
import dev.sagar.taskify.domain.entities.TaskList;
import dev.sagar.taskify.domain.entities.TaskPriority;
import dev.sagar.taskify.domain.entities.TaskStatus;
import dev.sagar.taskify.mappers.TaskMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskListMapperImplTest {

  @Mock private TaskMapper taskMapper;

  @InjectMocks private TaskListMapperImpl taskListMapper;

  private UUID taskListId;
  private TaskList taskList;
  private TaskListDto taskListDto;
  private Task task1;
  private Task task2;
  private TaskDto taskDto1;
  private TaskDto taskDto2;

  @BeforeEach
  void setUp() {
    taskListId = UUID.randomUUID();
    UUID taskId1 = UUID.randomUUID();
    UUID taskId2 = UUID.randomUUID();

    task1 =
        Task.builder()
            .id(taskId1)
            .title("Task 1")
            .status(TaskStatus.OPEN)
            .priority(TaskPriority.MEDIUM)
            .build();

    task2 =
        Task.builder()
            .id(taskId2)
            .title("Task 2")
            .status(TaskStatus.CLOSED)
            .priority(TaskPriority.HIGH)
            .build();

    taskDto1 = new TaskDto(taskId1, "Task 1", null, null, TaskPriority.MEDIUM, TaskStatus.OPEN);
    taskDto2 = new TaskDto(taskId2, "Task 2", null, null, TaskPriority.HIGH, TaskStatus.CLOSED);

    taskList =
        TaskList.builder()
            .id(taskListId)
            .title("Test List")
            .description("Test Description")
            .created(LocalDateTime.now())
            .updated(LocalDateTime.now())
            .tasks(List.of(task1, task2))
            .build();

    taskListDto =
        new TaskListDto(
            taskListId, "Test List", "Test Description", 2, 0.5, List.of(taskDto1, taskDto2));
  }

  @Test
  void fromDto_ShouldMapTaskListDtoToTaskList() {
    // Arrange
    when(taskMapper.fromDto(taskDto1)).thenReturn(task1);
    when(taskMapper.fromDto(taskDto2)).thenReturn(task2);

    // Act
    TaskList result = taskListMapper.fromDto(taskListDto);

    // Assert
    assertNotNull(result);
    assertEquals(taskListDto.id(), result.getId());
    assertEquals(taskListDto.title(), result.getTitle());
    assertEquals(taskListDto.description(), result.getDescription());
    assertNotNull(result.getTasks());
    assertEquals(2, result.getTasks().size());

    verify(taskMapper).fromDto(taskDto1);
    verify(taskMapper).fromDto(taskDto2);
  }

  @Test
  void fromDto_WithNullTasks_ShouldMapWithoutTasks() {
    // Arrange
    TaskListDto dtoWithoutTasks =
        new TaskListDto(taskListId, "Test List", "Test Description", null, null, null);

    // Act
    TaskList result = taskListMapper.fromDto(dtoWithoutTasks);

    // Assert
    assertNotNull(result);
    assertEquals(dtoWithoutTasks.id(), result.getId());
    assertEquals(dtoWithoutTasks.title(), result.getTitle());
    assertEquals(dtoWithoutTasks.description(), result.getDescription());
    assertNull(result.getTasks());
    verifyNoInteractions(taskMapper);
  }

  @Test
  void toDto_ShouldMapTaskListToTaskListDto() {
    // Arrange
    when(taskMapper.toDto(task1)).thenReturn(taskDto1);
    when(taskMapper.toDto(task2)).thenReturn(taskDto2);

    // Act
    TaskListDto result = taskListMapper.toDto(taskList);

    // Assert
    assertNotNull(result);
    assertEquals(taskList.getId(), result.id());
    assertEquals(taskList.getTitle(), result.title());
    assertEquals(taskList.getDescription(), result.description());
    assertEquals(2, result.count());
    assertEquals(0.5, result.progress());
    assertNotNull(result.tasks());
    assertEquals(2, result.tasks().size());

    verify(taskMapper).toDto(task1);
    verify(taskMapper).toDto(task2);
  }

  @Test
  void toDto_WithNullTasks_ShouldMapWithZeroCountAndNullProgress() {
    // Arrange
    TaskList taskListWithoutTasks =
        TaskList.builder()
            .id(taskListId)
            .title("Test List")
            .description("Test Description")
            .created(LocalDateTime.now())
            .updated(LocalDateTime.now())
            .tasks(null)
            .build();

    // Act
    TaskListDto result = taskListMapper.toDto(taskListWithoutTasks);

    // Assert
    assertNotNull(result);
    assertEquals(taskListWithoutTasks.getId(), result.id());
    assertEquals(taskListWithoutTasks.getTitle(), result.title());
    assertEquals(taskListWithoutTasks.getDescription(), result.description());
    assertEquals(0, result.count());
    assertNull(result.progress());
    assertNull(result.tasks());
    verifyNoInteractions(taskMapper);
  }

  @Test
  void toDto_WithEmptyTasks_ShouldMapWithZeroCountAndZeroProgress() {
    // Arrange
    TaskList taskListWithEmptyTasks =
        TaskList.builder()
            .id(taskListId)
            .title("Test List")
            .description("Test Description")
            .created(LocalDateTime.now())
            .updated(LocalDateTime.now())
            .tasks(List.of())
            .build();

    // Act
    TaskListDto result = taskListMapper.toDto(taskListWithEmptyTasks);

    // Assert
    assertNotNull(result);
    assertEquals(taskListWithEmptyTasks.getId(), result.id());
    assertEquals(taskListWithEmptyTasks.getTitle(), result.title());
    assertEquals(taskListWithEmptyTasks.getDescription(), result.description());
    assertEquals(0, result.count());
    assertNotNull(result.tasks());
    assertTrue(result.tasks().isEmpty());
    verifyNoInteractions(taskMapper);
  }
}
