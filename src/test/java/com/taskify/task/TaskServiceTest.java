package com.taskify.task;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.taskify.common.exceptions.TaskListNotFoundException;
import com.taskify.common.exceptions.TaskNotFoundException;
import com.taskify.task.domain.entities.Task;
import com.taskify.task.domain.entities.TaskPriority;
import com.taskify.task.domain.entities.TaskStatus;
import com.taskify.task.impl.TaskServiceImpl;
import com.taskify.tasklist.TaskListRepository;
import com.taskify.tasklist.domain.entities.TaskList;
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

  @InjectMocks private TaskServiceImpl taskService;

  private UUID taskListId;
  private UUID taskId;
  private TaskList taskList;
  private Task task;

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
  }

  @Test
  @DisplayName("Should create a task successfully")
  void shouldCreateTaskSuccessfully() {
    when(taskListRepository.findById(taskListId)).thenReturn(Optional.of(taskList));
    when(taskRepository.save(any(Task.class))).thenReturn(task);

    Task createdTask = taskService.createTask(taskListId, task);

    assertNotNull(createdTask);
    assertEquals(task.getTitle(), createdTask.getTitle());
    assertEquals(TaskStatus.OPEN, createdTask.getStatus());
    assertEquals(taskListId, createdTask.getTaskList().getId());
    verify(taskListRepository, times(1)).findById(taskListId);
    verify(taskRepository, times(1)).save(task);
  }

  @Test
  @DisplayName(
      "Should throw TaskListNotFoundException when creating task for non-existent task list")
  void shouldThrowTaskListNotFoundExceptionWhenCreatingTaskForNonExistentTaskList() {
    when(taskListRepository.findById(taskListId)).thenReturn(Optional.empty());

    assertThrows(TaskListNotFoundException.class, () -> taskService.createTask(taskListId, task));
    verify(taskListRepository, times(1)).findById(taskListId);
    verify(taskRepository, never()).save(any(Task.class));
  }

  @Test
  @DisplayName("Should list tasks for a given task list")
  void shouldListTasksForGivenTaskList() {
    when(taskRepository.findByTaskListId(taskListId)).thenReturn(List.of(task));

    List<Task> tasks = taskService.listTasks(taskListId);

    assertNotNull(tasks);
    assertFalse(tasks.isEmpty());
    assertEquals(1, tasks.size());
    assertEquals(task.getTitle(), tasks.get(0).getTitle());
    verify(taskRepository, times(1)).findByTaskListId(taskListId);
  }

  @Test
  @DisplayName("Should return empty list when no tasks found for a given task list")
  void shouldReturnEmptyListWhenNoTasksFoundForGivenTaskList() {
    when(taskRepository.findByTaskListId(taskListId)).thenReturn(Collections.emptyList());

    List<Task> tasks = taskService.listTasks(taskListId);

    assertNotNull(tasks);
    assertTrue(tasks.isEmpty());
    verify(taskRepository, times(1)).findByTaskListId(taskListId);
  }

  @Test
  @DisplayName("Should get a task by task list ID and task ID")
  void shouldGetTaskByTaskListIdAndTaskId() {
    when(taskRepository.findByTaskListIdAndId(taskListId, taskId)).thenReturn(Optional.of(task));

    Optional<Task> foundTask = taskService.getTask(taskListId, taskId);

    assertTrue(foundTask.isPresent());
    assertEquals(task.getTitle(), foundTask.get().getTitle());
    verify(taskRepository, times(1)).findByTaskListIdAndId(taskListId, taskId);
  }

  @Test
  @DisplayName("Should return empty optional when task not found by task list ID and task ID")
  void shouldReturnEmptyOptionalWhenTaskNotFoundByTaskListIdAndTaskId() {
    when(taskRepository.findByTaskListIdAndId(taskListId, taskId)).thenReturn(Optional.empty());

    Optional<Task> foundTask = taskService.getTask(taskListId, taskId);

    assertTrue(foundTask.isEmpty());
    verify(taskRepository, times(1)).findByTaskListIdAndId(taskListId, taskId);
  }

  @Test
  @DisplayName("Should update a task successfully")
  void shouldUpdateTaskSuccessfully() {
    Task updatedTaskDetails = new Task();
    updatedTaskDetails.setTitle("Updated Task Title");
    updatedTaskDetails.setDescription("Updated Description");
    updatedTaskDetails.setDueDate(LocalDateTime.now().plusDays(10));
    updatedTaskDetails.setPriority(TaskPriority.HIGH);
    updatedTaskDetails.setStatus(TaskStatus.CLOSED);

    when(taskRepository.findByTaskListIdAndId(taskListId, taskId)).thenReturn(Optional.of(task));
    when(taskRepository.save(any(Task.class))).thenReturn(task);

    Task result = taskService.updateTask(taskListId, taskId, updatedTaskDetails);

    assertNotNull(result);
    assertEquals(updatedTaskDetails.getTitle(), result.getTitle());
    assertEquals(updatedTaskDetails.getDescription(), result.getDescription());
    assertEquals(updatedTaskDetails.getDueDate(), result.getDueDate());
    assertEquals(updatedTaskDetails.getPriority(), result.getPriority());
    assertEquals(updatedTaskDetails.getStatus(), result.getStatus());
    verify(taskRepository, times(1)).findByTaskListIdAndId(taskListId, taskId);
    verify(taskRepository, times(1)).save(task);
  }

  @Test
  @DisplayName("Should throw TaskNotFoundException when updating non-existent task")
  void shouldThrowTaskNotFoundExceptionWhenUpdatingNonExistentTask() {
    Task updatedTaskDetails = new Task();
    when(taskRepository.findByTaskListIdAndId(taskListId, taskId)).thenReturn(Optional.empty());

    assertThrows(
        TaskNotFoundException.class,
        () -> taskService.updateTask(taskListId, taskId, updatedTaskDetails));
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
}
