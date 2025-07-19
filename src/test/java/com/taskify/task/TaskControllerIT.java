package com.taskify.task;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskify.common.BaseIT;
import com.taskify.task.domain.dtos.CreateTaskRequest;
import com.taskify.task.domain.dtos.UpdateTaskRequest;
import com.taskify.task.domain.entities.Task;
import com.taskify.task.domain.entities.TaskPriority;
import com.taskify.task.domain.entities.TaskStatus;
import com.taskify.tasklist.TaskListRepository;
import com.taskify.tasklist.domain.entities.TaskList;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

class TaskControllerIT extends BaseIT {

  @Autowired private WebApplicationContext webApplicationContext;

  @Autowired private TaskRepository taskRepository;

  @Autowired private TaskListRepository taskListRepository;

  @Autowired private ObjectMapper objectMapper;

  private MockMvc mockMvc;
  private TaskList taskList;
  private Task task1;
  private Task task2;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    taskRepository.deleteAll();
    taskListRepository.deleteAll();

    taskList = new TaskList();
    taskList.setTitle("Work Tasks");
    taskList.setDescription("Tasks related to work");
    taskList = taskListRepository.save(taskList);

    task1 = new Task();
    task1.setTitle("Complete Project Proposal");
    task1.setDescription("Write and finalize the project proposal document.");
    task1.setDueDate(LocalDateTime.now().plusDays(5));
    task1.setPriority(TaskPriority.HIGH);
    task1.setStatus(TaskStatus.OPEN);
    task1.setTaskList(taskList);
    task1 = taskRepository.save(task1);

    task2 = new Task();
    task2.setTitle("Schedule Team Meeting");
    task2.setDescription("Arrange a meeting with the team to discuss progress.");
    task2.setDueDate(LocalDateTime.now().plusDays(2));
    task2.setPriority(TaskPriority.MEDIUM);
    task2.setStatus(TaskStatus.OPEN);
    task2.setTaskList(taskList);
    task2 = taskRepository.save(task2);
  }

  @Test
  @DisplayName(
      "GET /api/v1/task-lists/{task_list_id}/tasks - Should list all tasks for a given task list")
  void shouldListAllTasksForGivenTaskList() throws Exception {
    mockMvc
        .perform(get("/api/v1/task-lists/{task_list_id}/tasks", taskList.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].title", is(task1.getTitle())))
        .andExpect(jsonPath("$[1].title", is(task2.getTitle())));
  }

  @Test
  @DisplayName(
      "GET /api/v1/task-lists/{task_list_id}/tasks - Should return 404 if task list not found")
  void shouldReturn404IfTaskListNotFoundWhenListingTasks() throws Exception {
    mockMvc
        .perform(get("/api/v1/task-lists/{task_list_id}/tasks", UUID.randomUUID()))
        .andExpect(status().isOk()) // Controller returns empty list if task list not found
        .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  @DisplayName(
      "POST /api/v1/task-lists/{task_list_id}/tasks - Should create a new task successfully")
  void shouldCreateNewTaskSuccessfully() throws Exception {
    CreateTaskRequest request =
        new CreateTaskRequest(
            "New Task",
            "Description for new task",
            LocalDateTime.now().plusDays(1),
            TaskPriority.LOW);

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
    CreateTaskRequest request =
        new CreateTaskRequest(
            "New Task",
            "Description for new task",
            LocalDateTime.now().plusDays(1),
            TaskPriority.LOW);

    mockMvc
        .perform(
            post("/api/v1/task-lists/{task_list_id}/tasks", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code", is("TASK_LIST_NOT_FOUND")));
  }

  @Test
  @DisplayName("GET /api/v1/task-lists/{task_list_id}/tasks/{task_id} - Should get a task by ID")
  void shouldGetTaskById() throws Exception {
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
    UpdateTaskRequest request =
        new UpdateTaskRequest(
            "Updated Title",
            "Updated Description",
            LocalDateTime.now().plusDays(15),
            TaskPriority.HIGH,
            TaskStatus.CLOSED);

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
    UpdateTaskRequest request =
        new UpdateTaskRequest(
            "Updated Title",
            "Updated Description",
            LocalDateTime.now().plusDays(15),
            TaskPriority.HIGH,
            TaskStatus.CLOSED);

    mockMvc
        .perform(
            put(
                    "/api/v1/task-lists/{task_list_id}/tasks/{task_id}",
                    taskList.getId(),
                    UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code", is("TASK_NOT_FOUND")));
  }

  @Test
  @DisplayName(
      "DELETE /api/v1/task-lists/{task_list_id}/tasks/{task_id} - Should delete a task successfully")
  void shouldDeleteTaskSuccessfully() throws Exception {
    mockMvc
        .perform(
            delete(
                "/api/v1/task-lists/{task_list_id}/tasks/{task_id}",
                taskList.getId(),
                task1.getId()))
        .andExpect(status().isNoContent());

    mockMvc
        .perform(
            get(
                "/api/v1/task-lists/{task_list_id}/tasks/{task_id}",
                taskList.getId(),
                task1.getId()))
        .andExpect(status().isNotFound());
  }
}
