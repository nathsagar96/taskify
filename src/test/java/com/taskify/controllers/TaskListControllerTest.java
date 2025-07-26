package com.taskify.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskify.dtos.CreateTaskListRequest;
import com.taskify.dtos.TaskListDto;
import com.taskify.dtos.UpdateTaskListRequest;
import com.taskify.entities.TaskList;
import com.taskify.exceptions.TaskListNotFoundException;
import com.taskify.mappers.TaskListMapper;
import com.taskify.services.TaskListService;
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
@WebMvcTest(controllers = TaskListController.class)
class TaskListControllerTest {

  @Autowired private ObjectMapper objectMapper;
  @Autowired private MockMvc mockMvc;
  @MockitoBean private TaskListService taskListService;
  @MockitoBean private TaskListMapper taskListMapper;

  private TaskList taskList1;
  private TaskList taskList2;

  @BeforeEach
  void setUp() {
    taskList1 = new TaskList();
    taskList1.setId(UUID.randomUUID());
    taskList1.setTitle("Personal Tasks");

    taskList2 = new TaskList();
    taskList2.setId(UUID.randomUUID());
    taskList2.setTitle("Work Tasks");
  }

  @Test
  @DisplayName("GET /api/v1/task-lists - Should list all task lists")
  void shouldListAllTaskLists() throws Exception {
    when(taskListService.listTaskLists()).thenReturn(java.util.List.of(taskList1, taskList2));
    when(taskListMapper.toDto(taskList1))
        .thenReturn(
            new TaskListDto(
                taskList1.getId(),
                taskList1.getTitle(),
                taskList1.getDescription(),
                0,
                0.0,
                java.util.List.of()));
    when(taskListMapper.toDto(taskList2))
        .thenReturn(
            new TaskListDto(
                taskList2.getId(),
                taskList2.getTitle(),
                taskList2.getDescription(),
                0,
                0.0,
                java.util.List.of()));

    mockMvc
        .perform(get("/api/v1/task-lists"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id", is(taskList1.getId().toString())))
        .andExpect(jsonPath("$[0].title", is(taskList1.getTitle())))
        .andExpect(jsonPath("$[1].id", is(taskList2.getId().toString())))
        .andExpect(jsonPath("$[1].title", is(taskList2.getTitle())));
  }

  @Test
  @DisplayName("POST /api/v1/task-lists - Should create a new task list successfully")
  void shouldCreateNewTaskListSuccessfully() throws Exception {
    CreateTaskListRequest request =
        new CreateTaskListRequest("New Task List", "New Task List Description");

    TaskList createdTaskList = new TaskList();
    createdTaskList.setId(UUID.randomUUID());
    createdTaskList.setTitle(request.title());
    createdTaskList.setDescription(request.description());

    when(taskListMapper.fromCreateRequest(any(CreateTaskListRequest.class)))
        .thenReturn(createdTaskList);
    when(taskListService.createTaskList(any(TaskList.class))).thenReturn(createdTaskList);
    when(taskListMapper.toDto(any(TaskList.class)))
        .thenReturn(
            new TaskListDto(
                createdTaskList.getId(),
                createdTaskList.getTitle(),
                createdTaskList.getDescription(),
                0,
                0.0,
                java.util.List.of()));

    mockMvc
        .perform(
            post("/api/v1/task-lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(createdTaskList.getId().toString())))
        .andExpect(jsonPath("$.title", is(request.title())));
  }

  @Test
  @DisplayName("GET /api/v1/task-lists/{id} - Should get a task list by ID")
  void shouldGetTaskListById() throws Exception {
    when(taskListService.getTaskList(taskList1.getId()))
        .thenReturn(java.util.Optional.of(taskList1));
    when(taskListMapper.toDto(taskList1))
        .thenReturn(
            new TaskListDto(
                taskList1.getId(),
                taskList1.getTitle(),
                taskList1.getDescription(),
                0,
                0.0,
                java.util.List.of()));

    mockMvc
        .perform(get("/api/v1/task-lists/{id}", taskList1.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(taskList1.getId().toString())))
        .andExpect(jsonPath("$.title", is(taskList1.getTitle())));
  }

  @Test
  @DisplayName("GET /api/v1/task-lists/{id} - Should return 404 if task list not found")
  void shouldReturn404IfTaskListNotFound() throws Exception {
    when(taskListService.getTaskList(any(UUID.class))).thenReturn(java.util.Optional.empty());

    mockMvc
        .perform(get("/api/v1/task-lists/{id}", UUID.randomUUID()))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("PUT /api/v1/task-lists/{id} - Should update an existing task list successfully")
  void shouldUpdateExistingTaskListSuccessfully() throws Exception {
    UpdateTaskListRequest request =
        new UpdateTaskListRequest("Updated Task List Name", "Updated Task List Description");

    TaskList updatedTaskList = new TaskList();
    updatedTaskList.setId(taskList1.getId());
    updatedTaskList.setTitle(request.title());
    updatedTaskList.setDescription(request.description());

    when(taskListMapper.fromUpdateRequest(any(UpdateTaskListRequest.class)))
        .thenReturn(updatedTaskList);
    when(taskListService.updateTaskList(any(UUID.class), any(TaskList.class)))
        .thenReturn(updatedTaskList);
    when(taskListMapper.toDto(any(TaskList.class)))
        .thenReturn(
            new TaskListDto(
                updatedTaskList.getId(),
                updatedTaskList.getTitle(),
                updatedTaskList.getDescription(),
                0,
                0.0,
                java.util.List.of()));

    mockMvc
        .perform(
            put("/api/v1/task-lists/{id}", taskList1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(updatedTaskList.getId().toString())))
        .andExpect(jsonPath("$.title", is(request.title())));
  }

  @Test
  @DisplayName(
      "PUT /api/v1/task-lists/{id} - Should return 404 if task list not found when updating")
  void shouldReturn404IfTaskListNotFoundWhenUpdating() throws Exception {
    UpdateTaskListRequest request =
        new UpdateTaskListRequest("Updated Task List Name", "Updated Task List Description");

    when(taskListMapper.fromUpdateRequest(any(UpdateTaskListRequest.class)))
        .thenReturn(new TaskList());
    when(taskListService.updateTaskList(any(UUID.class), any(TaskList.class)))
        .thenThrow(new TaskListNotFoundException("Task list not found"));

    mockMvc
        .perform(
            put("/api/v1/task-lists/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.detail", is("Task list not found")));
  }

  @Test
  @DisplayName("DELETE /api/v1/task-lists/{id} - Should delete a task list successfully")
  void shouldDeleteTaskListSuccessfully() throws Exception {
    doNothing().when(taskListService).deleteTaskList(taskList1.getId());

    mockMvc
        .perform(delete("/api/v1/task-lists/{id}", taskList1.getId()))
        .andExpect(status().isNoContent());

    when(taskListService.getTaskList(taskList1.getId()))
        .thenThrow(new TaskListNotFoundException("Task list not found"));

    mockMvc
        .perform(get("/api/v1/task-lists/{id}", taskList1.getId()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.detail", is("Task list not found")));
  }
}
