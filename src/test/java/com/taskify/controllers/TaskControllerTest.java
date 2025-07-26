package com.taskify.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.taskify.services.TaskService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@WebMvcTest(controllers = TaskController.class)
class TaskControllerTest {

  @Autowired private ObjectMapper objectMapper;
  @Autowired private MockMvc mockMvc;
  @MockitoBean private TaskService taskService;
  @MockitoBean private TaskMapper taskMapper;

  private TaskList taskList;
  private Task task1;
  private Task task2;
  private TaskDto taskDto1;
  private TaskDto taskDto2;

  @BeforeEach
  void setUp() {
    taskList = new TaskList();
    taskList.setId(UUID.randomUUID());
    taskList.setTitle("Work Tasks");
    taskList.setDescription("Tasks related to work");

    task1 = new Task();
    task1.setId(UUID.randomUUID());
    task1.setTitle("Complete Project Proposal");
    task1.setDescription("Write and finalize the project proposal document.");
    task1.setDueDate(LocalDateTime.now().plusDays(5));
    task1.setPriority(TaskPriority.HIGH);
    task1.setStatus(TaskStatus.OPEN);
    task1.setTaskList(taskList);

    task2 = new Task();
    task2.setId(UUID.randomUUID());
    task2.setTitle("Schedule Team Meeting");
    task2.setDescription("Arrange a meeting with the team to discuss progress.");
    task2.setDueDate(LocalDateTime.now().plusDays(2));
    task2.setPriority(TaskPriority.MEDIUM);
    task2.setStatus(TaskStatus.OPEN);
    task2.setTaskList(taskList);

    taskDto1 =
        new TaskDto(
            task1.getId(),
            task1.getTitle(),
            task1.getDescription(),
            task1.getDueDate(),
            task1.getPriority(),
            task1.getStatus());

    taskDto2 =
        new TaskDto(
            task2.getId(),
            task2.getTitle(),
            task2.getDescription(),
            task2.getDueDate(),
            task2.getPriority(),
            task2.getStatus());
  }

  @Test
  @DisplayName(
      "GET /api/v1/task-lists/{task_list_id}/tasks - Should list all tasks for a given task list")
  void shouldListAllTasksForGivenTaskList() throws Exception {
    // Arrange
    when(taskService.listTasks(taskList.getId())).thenReturn(List.of(task1, task2));
    when(taskMapper.toDto(task1)).thenReturn(taskDto1);
    when(taskMapper.toDto(task2)).thenReturn(taskDto2);

    // Act & Assert
    mockMvc
        .perform(get("/api/v1/task-lists/{task_list_id}/tasks", taskList.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].title", is(task1.getTitle())))
        .andExpect(jsonPath("$[1].title", is(task2.getTitle())));
  }

  @Test
  @DisplayName(
      "GET /api/v1/task-lists/{task_list_id}/tasks - Should return empty list if task list not found")
  void shouldReturnEmptyListIfTaskListNotFoundWhenListingTasks() throws Exception {
    // Arrange
    UUID nonExistentTaskListId = UUID.randomUUID();
    when(taskService.listTasks(nonExistentTaskListId)).thenReturn(List.of());

    // Act & Assert
    mockMvc
        .perform(get("/api/v1/task-lists/{task_list_id}/tasks", nonExistentTaskListId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  @DisplayName(
      "POST /api/v1/task-lists/{task_list_id}/tasks - Should create a new task successfully")
  void shouldCreateNewTaskSuccessfully() throws Exception {
    // Arrange
    CreateTaskRequest request =
        new CreateTaskRequest(
            "New Task",
            "Description for new task",
            LocalDateTime.now().plusDays(1),
            TaskPriority.LOW);

    Task newTask = new Task();
    newTask.setId(UUID.randomUUID());
    newTask.setTitle(request.title());
    newTask.setDescription(request.description());
    newTask.setDueDate(request.dueDate());
    newTask.setPriority(request.priority());
    newTask.setStatus(TaskStatus.OPEN);
    newTask.setTaskList(taskList);

    TaskDto newTaskDto =
        new TaskDto(
            newTask.getId(),
            newTask.getTitle(),
            newTask.getDescription(),
            newTask.getDueDate(),
            newTask.getPriority(),
            newTask.getStatus());

    when(taskService.createTask(eq(taskList.getId()), any(Task.class))).thenReturn(newTask);
    when(taskMapper.fromCreateRequest(request)).thenReturn(newTask);
    when(taskMapper.toDto(newTask)).thenReturn(newTaskDto);

    // Act & Assert
    mockMvc
        .perform(
            post("/api/v1/task-lists/{task_list_id}/tasks", taskList.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.title", is(request.title())))
        .andExpect(jsonPath("$.status", is(TaskStatus.OPEN.name())));
  }

  @Test
  @DisplayName(
      "POST /api/v1/task-lists/{task_list_id}/tasks - Should return 404 if task list not found when creating task")
  void shouldReturn404IfTaskListNotFoundWhenCreatingTask() throws Exception {
    // Arrange
    CreateTaskRequest request =
        new CreateTaskRequest(
            "New Task",
            "Description for new task",
            LocalDateTime.now().plusDays(1),
            TaskPriority.LOW);

    UUID nonExistentTaskListId = UUID.randomUUID();
    Task taskToCreate = new Task();
    taskToCreate.setTitle(request.title());
    taskToCreate.setDescription(request.description());
    taskToCreate.setDueDate(request.dueDate());
    taskToCreate.setPriority(request.priority());

    when(taskMapper.fromCreateRequest(request)).thenReturn(taskToCreate);
    when(taskService.createTask(eq(nonExistentTaskListId), eq(taskToCreate)))
        .thenThrow(
            new TaskListNotFoundException("Task List not found with ID: " + nonExistentTaskListId));

    // Act & Assert
    mockMvc
        .perform(
            post("/api/v1/task-lists/{task_list_id}/tasks", nonExistentTaskListId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(
            jsonPath("$.detail", is("Task List not found with ID: " + nonExistentTaskListId)));
  }

  @Test
  @DisplayName("GET /api/v1/task-lists/{task_list_id}/tasks/{task_id} - Should get a task by ID")
  void shouldGetTaskById() throws Exception {
    // Arrange
    when(taskService.getTask(taskList.getId(), task1.getId())).thenReturn(Optional.of(task1));
    when(taskMapper.toDto(task1)).thenReturn(taskDto1);

    // Act & Assert
    mockMvc
        .perform(
            get(
                "/api/v1/task-lists/{task_list_id}/tasks/{task_id}",
                taskList.getId(),
                task1.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title", is(task1.getTitle())));
  }

  @Test
  @DisplayName(
      "GET /api/v1/task-lists/{task_list_id}/tasks/{task_id} - Should return 404 if task not found")
  void shouldReturn404IfTaskNotFound() throws Exception {
    // Arrange
    when(taskService.getTask(taskList.getId(), UUID.randomUUID())).thenReturn(Optional.empty());

    // Act & Assert
    mockMvc
        .perform(
            get(
                "/api/v1/task-lists/{task_list_id}/tasks/{task_id}",
                taskList.getId(),
                UUID.randomUUID()))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName(
      "PUT /api/v1/task-lists/{task_list_id}/tasks/{task_id} - Should update an existing task successfully")
  void shouldUpdateExistingTaskSuccessfully() throws Exception {
    // Arrange
    UpdateTaskRequest request =
        new UpdateTaskRequest(
            "Updated Title",
            "Updated Description",
            LocalDateTime.now().plusDays(15),
            TaskPriority.HIGH,
            TaskStatus.CLOSED);

    Task taskToUpdate = new Task();
    taskToUpdate.setTitle(request.title());
    taskToUpdate.setDescription(request.description());
    taskToUpdate.setDueDate(request.dueDate());
    taskToUpdate.setPriority(request.priority());
    taskToUpdate.setStatus(request.status());

    Task updatedTask = new Task();
    updatedTask.setId(task1.getId());
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

    when(taskMapper.fromUpdateRequest(request)).thenReturn(taskToUpdate);
    when(taskService.updateTask(eq(taskList.getId()), eq(task1.getId()), eq(taskToUpdate)))
        .thenReturn(updatedTask);
    when(taskMapper.toDto(updatedTask)).thenReturn(updatedTaskDto);

    // Act & Assert
    mockMvc
        .perform(
            put(
                    "/api/v1/task-lists/{task_list_id}/tasks/{task_id}",
                    taskList.getId(),
                    task1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title", is(request.title())))
        .andExpect(jsonPath("$.description", is(request.description())))
        .andExpect(jsonPath("$.priority", is(request.priority().name())))
        .andExpect(jsonPath("$.status", is(request.status().name())));
  }

  @Test
  @DisplayName(
      "PUT /api/v1/task-lists/{task_list_id}/tasks/{task_id} - Should return 404 if task not found when updating")
  void shouldReturn404IfTaskNotFoundWhenUpdating() throws Exception {
    // Arrange
    UpdateTaskRequest request =
        new UpdateTaskRequest(
            "Updated Title",
            "Updated Description",
            LocalDateTime.now().plusDays(15),
            TaskPriority.HIGH,
            TaskStatus.CLOSED);

    UUID nonExistentTaskId = UUID.randomUUID();
    Task taskToUpdate = new Task();
    taskToUpdate.setTitle(request.title());
    taskToUpdate.setDescription(request.description());
    taskToUpdate.setDueDate(request.dueDate());
    taskToUpdate.setPriority(request.priority());
    taskToUpdate.setStatus(request.status());

    when(taskMapper.fromUpdateRequest(request)).thenReturn(taskToUpdate);
    when(taskService.updateTask(eq(taskList.getId()), eq(nonExistentTaskId), eq(taskToUpdate)))
        .thenThrow(
            new TaskNotFoundException(
                "Task not found with ID: "
                    + nonExistentTaskId
                    + " in Task List: "
                    + taskList.getId()));

    // Act & Assert
    mockMvc
        .perform(
            put(
                    "/api/v1/task-lists/{task_list_id}/tasks/{task_id}",
                    taskList.getId(),
                    nonExistentTaskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(
            jsonPath(
                "$.detail",
                is(
                    "Task not found with ID: "
                        + nonExistentTaskId
                        + " in Task List: "
                        + taskList.getId())));
  }

  @Test
  @DisplayName(
      "DELETE /api/v1/task-lists/{task_list_id}/tasks/{task_id} - Should delete a task successfully")
  void shouldDeleteTaskSuccessfully() throws Exception {
    // Arrange
    doNothing().when(taskService).deleteTask(taskList.getId(), task1.getId());

    // Act & Assert
    mockMvc
        .perform(
            delete(
                "/api/v1/task-lists/{task_list_id}/tasks/{task_id}",
                taskList.getId(),
                task1.getId()))
        .andExpect(status().isNoContent());

    // Verify deletion
    when(taskService.getTask(taskList.getId(), task1.getId())).thenReturn(Optional.empty());

    mockMvc
        .perform(
            get(
                "/api/v1/task-lists/{task_list_id}/tasks/{task_id}",
                taskList.getId(),
                task1.getId()))
        .andExpect(status().isNotFound());
  }
}
