package com.taskify.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.taskify.dtos.CreateTaskRequest;
import com.taskify.dtos.TaskDto;
import com.taskify.dtos.UpdateTaskRequest;
import com.taskify.entities.Task;
import com.taskify.entities.TaskList;
import com.taskify.entities.TaskPriority;
import com.taskify.entities.TaskStatus;
import com.taskify.exceptions.TaskListNotFoundException;
import com.taskify.exceptions.TaskNotFoundException;
import com.taskify.mappers.TaskMapper;
import com.taskify.repositories.TaskListRepository;
import com.taskify.repositories.TaskRepository;
import com.taskify.services.impl.TaskServiceImpl;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

  @Mock private TaskRepository taskRepository;
  @Mock private TaskListRepository taskListRepository;
  @Mock private TaskMapper taskMapper;
  @InjectMocks private TaskServiceImpl taskService;

  private UUID taskListId;
  private UUID taskId;
  private TaskList taskList;
  private Task task;
  private TaskDto taskDto;

  @BeforeEach
  void setUp() {
    taskListId = UUID.randomUUID();
    taskId = UUID.randomUUID();
    taskList = new TaskList();
    taskList.setId(taskListId);
    taskList.setTitle("Test Task List");

    task = new Task();
    task.setId(taskId);
    task.setTitle("Test Task");
    task.setDescription("Description for test task");
    task.setDueDate(LocalDateTime.now().plusDays(7));
    task.setPriority(TaskPriority.MEDIUM);
    task.setStatus(TaskStatus.OPEN);
    task.setTaskList(taskList);

    taskDto =
        new TaskDto(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getDueDate(),
            task.getPriority(),
            task.getStatus());
  }

  @Test
  @DisplayName("Should create a task successfully")
  void shouldCreateTaskSuccessfully() {
    CreateTaskRequest request =
        new CreateTaskRequest(
            "New Task",
            "Description for new task",
            LocalDateTime.now().plusDays(1),
            TaskPriority.LOW);

    when(taskListRepository.findById(taskListId)).thenReturn(Optional.of(taskList));
    when(taskMapper.fromCreateRequest(request)).thenReturn(task);
    when(taskRepository.save(any(Task.class))).thenReturn(task);
    when(taskMapper.toDto(task)).thenReturn(taskDto);

    TaskDto createdTask = taskService.createTask(taskListId, request);

    assertNotNull(createdTask);
    assertEquals(taskDto.title(), createdTask.title());
    assertEquals(TaskStatus.OPEN, createdTask.status());
    verify(taskListRepository, times(1)).findById(taskListId);
    verify(taskRepository, times(1)).save(task);
    verify(taskMapper, times(1)).fromCreateRequest(request);
    verify(taskMapper, times(1)).toDto(task);
  }

  @Test
  @DisplayName(
      "Should throw TaskListNotFoundException when creating task for non-existent task list")
  void shouldThrowTaskListNotFoundExceptionWhenCreatingTaskForNonExistentTaskList() {
    CreateTaskRequest request =
        new CreateTaskRequest(
            "New Task",
            "Description for new task",
            LocalDateTime.now().plusDays(1),
            TaskPriority.LOW);

    when(taskListRepository.findById(taskListId)).thenReturn(Optional.empty());

    assertThrows(
        TaskListNotFoundException.class, () -> taskService.createTask(taskListId, request));
    verify(taskListRepository, times(1)).findById(taskListId);
    verify(taskRepository, never()).save(any(Task.class));
  }

  @Test
  @DisplayName("Should list tasks for a given task list")
  void shouldListTasksForGivenTaskList() {
    when(taskRepository.findByTaskListId(taskListId)).thenReturn(List.of(task));
    when(taskMapper.toDto(task)).thenReturn(taskDto);

    List<TaskDto> tasks = taskService.listTasks(taskListId);

    assertNotNull(tasks);
    assertFalse(tasks.isEmpty());
    assertEquals(1, tasks.size());
    assertEquals(taskDto.title(), tasks.getFirst().title());
    verify(taskRepository, times(1)).findByTaskListId(taskListId);
    verify(taskMapper, times(1)).toDto(task);
  }

  @Test
  @DisplayName("Should return empty list when no tasks found for a given task list")
  void shouldReturnEmptyListWhenNoTasksFoundForGivenTaskList() {
    when(taskRepository.findByTaskListId(taskListId)).thenReturn(Collections.emptyList());

    List<TaskDto> tasks = taskService.listTasks(taskListId);

    assertNotNull(tasks);
    assertTrue(tasks.isEmpty());
    verify(taskRepository, times(1)).findByTaskListId(taskListId);
    verify(taskMapper, never()).toDto(any(Task.class));
  }

  @Test
  @DisplayName("Should get a task by task list ID and task ID")
  void shouldGetTaskByTaskListIdAndTaskId() {
    when(taskRepository.findByTaskListIdAndId(taskListId, taskId)).thenReturn(Optional.of(task));
    when(taskMapper.toDto(task)).thenReturn(taskDto);

    TaskDto foundTask = taskService.getTask(taskListId, taskId);

    assertNotNull(foundTask);
    assertEquals(taskDto.title(), foundTask.title());
    verify(taskRepository, times(1)).findByTaskListIdAndId(taskListId, taskId);
    verify(taskMapper, times(1)).toDto(task);
  }

  @Test
  @DisplayName("Should throw TaskNotFoundException when task not found by task list ID and task ID")
  void shouldThrowTaskNotFoundExceptionWhenTaskNotFoundByTaskListIdAndTaskId() {
    when(taskRepository.findByTaskListIdAndId(taskListId, taskId)).thenReturn(Optional.empty());

    assertThrows(TaskNotFoundException.class, () -> taskService.getTask(taskListId, taskId));
    verify(taskRepository, times(1)).findByTaskListIdAndId(taskListId, taskId);
    verify(taskMapper, never()).toDto(any(Task.class));
  }

  @Test
  @DisplayName("Should update a task successfully")
  void shouldUpdateTaskSuccessfully() {
    UpdateTaskRequest request =
        new UpdateTaskRequest(
            "Updated Task Title",
            "Updated Description",
            LocalDateTime.now().plusDays(10),
            TaskPriority.HIGH,
            TaskStatus.CLOSED);

    Task updatedTask = new Task();
    updatedTask.setId(taskId);
    updatedTask.setTitle(request.title());
    updatedTask.setDescription(request.description());
    updatedTask.setDueDate(request.dueDate());
    updatedTask.setPriority(request.priority());
    updatedTask.setStatus(request.status());
    updatedTask.setTaskList(taskList);

    TaskDto updatedTaskDto =
        new TaskDto(
            updatedTask.getId(),
            updatedTask.getTitle(),
            updatedTask.getDescription(),
            updatedTask.getDueDate(),
            updatedTask.getPriority(),
            updatedTask.getStatus());

    when(taskRepository.findByTaskListIdAndId(taskListId, taskId)).thenReturn(Optional.of(task));
    when(taskMapper.fromUpdateRequest(request)).thenReturn(updatedTask);
    when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
    when(taskMapper.toDto(updatedTask)).thenReturn(updatedTaskDto);

    TaskDto result = taskService.updateTask(taskListId, taskId, request);

    assertNotNull(result);
    assertEquals(updatedTaskDto.title(), result.title());
    assertEquals(updatedTaskDto.description(), result.description());
    assertEquals(updatedTaskDto.dueDate(), result.dueDate());
    assertEquals(updatedTaskDto.priority(), result.priority());
    assertEquals(updatedTaskDto.status(), result.status());
    verify(taskRepository, times(1)).findByTaskListIdAndId(taskListId, taskId);
    verify(taskRepository, times(1)).save(task);
    verify(taskMapper, times(1)).fromUpdateRequest(request);
    verify(taskMapper, times(1)).toDto(updatedTask);
  }

  @Test
  @DisplayName("Should throw TaskNotFoundException when updating non-existent task")
  void shouldThrowTaskNotFoundExceptionWhenUpdatingNonExistentTask() {
    UpdateTaskRequest request =
        new UpdateTaskRequest(
            "Updated Task Title",
            "Updated Description",
            LocalDateTime.now().plusDays(10),
            TaskPriority.HIGH,
            TaskStatus.CLOSED);

    when(taskRepository.findByTaskListIdAndId(taskListId, taskId)).thenReturn(Optional.empty());

    assertThrows(
        TaskNotFoundException.class, () -> taskService.updateTask(taskListId, taskId, request));
    verify(taskRepository, times(1)).findByTaskListIdAndId(taskListId, taskId);
    verify(taskRepository, never()).save(any(Task.class));
  }

  @Test
  @DisplayName("Should delete a task successfully")
  void shouldDeleteTaskSuccessfully() {
    doNothing().when(taskRepository).deleteByTaskListIdAndId(taskListId, taskId);

    taskService.deleteTask(taskListId, taskId);

    verify(taskRepository, times(1)).deleteByTaskListIdAndId(taskListId, taskId);
  }

  @Test
  @DisplayName("Should update task with partial fields successfully")
  void shouldUpdateTaskWithPartialFieldsSuccessfully() {
    UpdateTaskRequest request = new UpdateTaskRequest("Updated Task Title", null, null, null, null);

    Task updatedTask = new Task();
    updatedTask.setId(taskId);
    updatedTask.setTitle(request.title());
    updatedTask.setDescription(task.getDescription());
    updatedTask.setDueDate(task.getDueDate());
    updatedTask.setPriority(task.getPriority());
    updatedTask.setStatus(task.getStatus());
    updatedTask.setTaskList(taskList);

    TaskDto updatedTaskDto =
        new TaskDto(
            updatedTask.getId(),
            updatedTask.getTitle(),
            updatedTask.getDescription(),
            updatedTask.getDueDate(),
            updatedTask.getPriority(),
            updatedTask.getStatus());

    when(taskRepository.findByTaskListIdAndId(taskListId, taskId)).thenReturn(Optional.of(task));
    when(taskMapper.fromUpdateRequest(request)).thenReturn(updatedTask);
    when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
    when(taskMapper.toDto(updatedTask)).thenReturn(updatedTaskDto);

    TaskDto result = taskService.updateTask(taskListId, taskId, request);

    assertNotNull(result);
    assertEquals(updatedTaskDto.title(), result.title());
    assertEquals(taskDto.description(), result.description());
    assertEquals(taskDto.dueDate(), result.dueDate());
    assertEquals(taskDto.priority(), result.priority());
    assertEquals(taskDto.status(), result.status());
    verify(taskRepository, times(1)).findByTaskListIdAndId(taskListId, taskId);
    verify(taskRepository, times(1)).save(task);
    verify(taskMapper, times(1)).fromUpdateRequest(request);
    verify(taskMapper, times(1)).toDto(updatedTask);
  }

  @Test
  @DisplayName("Should update task due date successfully through updateTask")
  void shouldUpdateTaskDueDateSuccessfullyThroughUpdateTask() {
    UpdateTaskRequest request =
        new UpdateTaskRequest(null, null, LocalDateTime.now().plusDays(5), null, null);

    Task updatedTask = new Task();
    updatedTask.setId(taskId);
    updatedTask.setTitle(task.getTitle());
    updatedTask.setDescription(task.getDescription());
    updatedTask.setDueDate(request.dueDate());
    updatedTask.setPriority(task.getPriority());
    updatedTask.setStatus(task.getStatus());
    updatedTask.setTaskList(taskList);

    TaskDto updatedTaskDto =
        new TaskDto(
            updatedTask.getId(),
            updatedTask.getTitle(),
            updatedTask.getDescription(),
            updatedTask.getDueDate(),
            updatedTask.getPriority(),
            updatedTask.getStatus());

    when(taskRepository.findByTaskListIdAndId(taskListId, taskId)).thenReturn(Optional.of(task));
    when(taskMapper.fromUpdateRequest(request)).thenReturn(updatedTask);
    when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
    when(taskMapper.toDto(updatedTask)).thenReturn(updatedTaskDto);

    TaskDto result = taskService.updateTask(taskListId, taskId, request);

    assertNotNull(result);
    assertEquals(updatedTaskDto.dueDate(), result.dueDate());
    verify(taskRepository, times(1)).findByTaskListIdAndId(taskListId, taskId);
    verify(taskRepository, times(1)).save(task);
    verify(taskMapper, times(1)).fromUpdateRequest(request);
    verify(taskMapper, times(1)).toDto(updatedTask);
  }

  @Test
  @DisplayName("Should update task priority successfully through updateTask")
  void shouldUpdateTaskPrioritySuccessfullyThroughUpdateTask() {
    UpdateTaskRequest request = new UpdateTaskRequest(null, null, null, TaskPriority.HIGH, null);

    Task updatedTask = new Task();
    updatedTask.setId(taskId);
    updatedTask.setTitle(task.getTitle());
    updatedTask.setDescription(task.getDescription());
    updatedTask.setDueDate(task.getDueDate());
    updatedTask.setPriority(request.priority());
    updatedTask.setStatus(task.getStatus());
    updatedTask.setTaskList(taskList);

    TaskDto updatedTaskDto =
        new TaskDto(
            updatedTask.getId(),
            updatedTask.getTitle(),
            updatedTask.getDescription(),
            updatedTask.getDueDate(),
            updatedTask.getPriority(),
            updatedTask.getStatus());

    when(taskRepository.findByTaskListIdAndId(taskListId, taskId)).thenReturn(Optional.of(task));
    when(taskMapper.fromUpdateRequest(request)).thenReturn(updatedTask);
    when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
    when(taskMapper.toDto(updatedTask)).thenReturn(updatedTaskDto);

    TaskDto result = taskService.updateTask(taskListId, taskId, request);

    assertNotNull(result);
    assertEquals(updatedTaskDto.priority(), result.priority());
    verify(taskRepository, times(1)).findByTaskListIdAndId(taskListId, taskId);
    verify(taskRepository, times(1)).save(task);
    verify(taskMapper, times(1)).fromUpdateRequest(request);
    verify(taskMapper, times(1)).toDto(updatedTask);
  }

  @Test
  @DisplayName(
      "Should throw IllegalArgumentException when updating due date with past date through updateTask")
  void shouldThrowIllegalArgumentExceptionWhenUpdatingDueDateWithPastDateThroughUpdateTask() {
    UpdateTaskRequest request =
        new UpdateTaskRequest(null, null, LocalDateTime.now().minusDays(1), null, null);

    Task updatedTask = new Task();
    updatedTask.setDueDate(request.dueDate());

    when(taskRepository.findByTaskListIdAndId(taskListId, taskId)).thenReturn(Optional.of(task));
    when(taskMapper.fromUpdateRequest(request)).thenReturn(updatedTask);

    assertThrows(
        IllegalArgumentException.class, () -> taskService.updateTask(taskListId, taskId, request));
    verify(taskRepository, times(1)).findByTaskListIdAndId(taskListId, taskId);
    verify(taskRepository, never()).save(any(Task.class));
  }
}
