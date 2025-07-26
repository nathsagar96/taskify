package com.taskify.mappers;

import static org.junit.jupiter.api.Assertions.*;

import com.taskify.dtos.CreateTaskRequest;
import com.taskify.dtos.TaskDto;
import com.taskify.dtos.UpdateTaskRequest;
import com.taskify.entities.Task;
import com.taskify.entities.TaskList;
import com.taskify.entities.TaskPriority;
import com.taskify.entities.TaskStatus;
import com.taskify.mappers.impl.TaskMapperImpl;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskMapperTest {

  @InjectMocks private TaskMapperImpl taskMapper;

  @Test
  @DisplayName("Should map Task entity to TaskDto")
  void shouldMapTaskEntityToTaskDto() {
    UUID taskId = UUID.randomUUID();
    UUID taskListId = UUID.randomUUID();
    TaskList taskList = new TaskList();
    taskList.setId(taskListId);
    taskList.setTitle("Test Task List");

    Task task = new Task();
    task.setId(taskId);
    task.setTitle("Test Task");
    task.setDescription("Description");
    task.setDueDate(LocalDateTime.of(2024, 12, 31, 23, 59));
    task.setPriority(TaskPriority.HIGH);
    task.setStatus(TaskStatus.OPEN);
    task.setTaskList(taskList);
    task.setCreated(LocalDateTime.now());
    task.setUpdated(LocalDateTime.now());

    TaskDto taskDto = taskMapper.toDto(task);

    assertNotNull(taskDto);
    assertEquals(task.getId(), taskDto.id());
    assertEquals(task.getTitle(), taskDto.title());
    assertEquals(task.getDescription(), taskDto.description());
    assertEquals(task.getDueDate(), taskDto.dueDate());
    assertEquals(task.getPriority().name(), taskDto.priority().name());
    assertEquals(task.getStatus().name(), taskDto.status().name());
  }

  @Test
  @DisplayName("Should map CreateTaskRequest to Task entity")
  void shouldMapCreateTaskRequestToTaskEntity() {
    CreateTaskRequest request =
        new CreateTaskRequest(
            "New Task", "New Description", LocalDateTime.of(2025, 1, 1, 10, 0), TaskPriority.LOW);

    Task task = taskMapper.fromCreateRequest(request);

    assertNotNull(task);
    assertEquals(request.title(), task.getTitle());
    assertEquals(request.description(), task.getDescription());
    assertEquals(request.dueDate(), task.getDueDate());
    assertEquals(TaskPriority.LOW, task.getPriority());
    assertNull(task.getId()); // ID should be null for new entity
    assertNull(task.getStatus()); // Status should be set by service
    assertNull(task.getTaskList()); // TaskList should be set by service
  }

  @Test
  @DisplayName("Should map UpdateTaskRequest to Task entity")
  void shouldMapUpdateTaskRequestToTaskEntity() {
    UpdateTaskRequest request =
        new UpdateTaskRequest(
            "Updated Task",
            "Updated Description",
            LocalDateTime.of(2025, 2, 2, 11, 0),
            TaskPriority.MEDIUM,
            TaskStatus.OPEN);

    Task task = taskMapper.fromUpdateRequest(request);

    assertNotNull(task);
    assertEquals(request.title(), task.getTitle());
    assertEquals(request.description(), task.getDescription());
    assertEquals(request.dueDate(), task.getDueDate());
    assertEquals(TaskPriority.MEDIUM, task.getPriority());
    assertEquals(TaskStatus.OPEN, task.getStatus());
    assertNull(task.getId()); // ID should be null for mapping
    assertNull(task.getTaskList()); // TaskList should be null for mapping
  }
}
