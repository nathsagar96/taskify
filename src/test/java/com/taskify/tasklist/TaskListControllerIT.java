package com.taskify.tasklist;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskify.common.BaseIT;
import com.taskify.tasklist.domain.dtos.CreateTaskListRequest;
import com.taskify.tasklist.domain.dtos.UpdateTaskListRequest;
import com.taskify.tasklist.domain.entities.TaskList;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

class TaskListControllerIT extends BaseIT {

  @Autowired private WebApplicationContext webApplicationContext;

  @Autowired private TaskListRepository taskListRepository;

  @Autowired private ObjectMapper objectMapper;

  private MockMvc mockMvc;
  private TaskList taskList1;
  private TaskList taskList2;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    taskListRepository.deleteAll();

    taskList1 = new TaskList();
    taskList1.setTitle("Personal Tasks");
    taskList1 = taskListRepository.save(taskList1);

    taskList2 = new TaskList();
    taskList2.setTitle("Work Tasks");
    taskList2 = taskListRepository.save(taskList2);
  }

  @Test
  @Transactional(readOnly = true)
  @DisplayName("GET /api/v1/task-lists - Should list all task lists")
  void shouldListAllTaskLists() throws Exception {
    mockMvc
        .perform(get("/api/v1/task-lists"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].title", is(taskList1.getTitle())))
        .andExpect(jsonPath("$[1].title", is(taskList2.getTitle())));
  }

  @Test
  @DisplayName("POST /api/v1/task-lists - Should create a new task list successfully")
  void shouldCreateNewTaskListSuccessfully() throws Exception {
    CreateTaskListRequest request =
        new CreateTaskListRequest("New Task List", "New Task List Description");

    mockMvc
        .perform(
            post("/api/v1/task-lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.title", is(request.title())));
  }

  @Test
  @DisplayName("GET /api/v1/task-lists/{id} - Should get a task list by ID")
  void shouldGetTaskListById() throws Exception {
    mockMvc
        .perform(get("/api/v1/task-lists/{id}", taskList1.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title", is(taskList1.getTitle())));
  }

  @Test
  @DisplayName("GET /api/v1/task-lists/{id} - Should return 404 if task list not found")
  void shouldReturn404IfTaskListNotFound() throws Exception {
    mockMvc
        .perform(get("/api/v1/task-lists/{id}", UUID.randomUUID()))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("PUT /api/v1/task-lists/{id} - Should update an existing task list successfully")
  void shouldUpdateExistingTaskListSuccessfully() throws Exception {
    UpdateTaskListRequest request =
        new UpdateTaskListRequest("Updated Task List Name", "Updated Task List Description");

    mockMvc
        .perform(
            put("/api/v1/task-lists/{id}", taskList1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title", is(request.title())));
  }

  @Test
  @DisplayName(
      "PUT /api/v1/task-lists/{id} - Should return 404 if task list not found when updating")
  void shouldReturn404IfTaskListNotFoundWhenUpdating() throws Exception {
    UpdateTaskListRequest request =
        new UpdateTaskListRequest("Updated Task List Name", "Updated Task List Name");

    mockMvc
        .perform(
            put("/api/v1/task-lists/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code", is("TASK_LIST_NOT_FOUND")));
  }

  @Test
  @DisplayName("DELETE /api/v1/task-lists/{id} - Should delete a task list successfully")
  void shouldDeleteTaskListSuccessfully() throws Exception {
    mockMvc
        .perform(delete("/api/v1/task-lists/{id}", taskList1.getId()))
        .andExpect(status().isNoContent());

    mockMvc
        .perform(get("/api/v1/task-lists/{id}", taskList1.getId()))
        .andExpect(status().isNotFound());
  }
}
