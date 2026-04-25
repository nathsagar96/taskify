package com.taskify.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.taskify.dtos.CreateTaskListRequest;
import com.taskify.dtos.TaskListDto;
import com.taskify.dtos.UpdateTaskListRequest;
import com.taskify.entities.TaskList;
import com.taskify.exceptions.TaskListNotFoundException;
import com.taskify.services.TaskListService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.cache.test.autoconfigure.AutoConfigureCache;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@AutoConfigureCache
@WebMvcTest(controllers = TaskListController.class)
@DisplayName("TaskListController - API Tests")
class TaskListControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskListService taskListService;

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

    @Nested
    @DisplayName("GET /api/v1/task-lists")
    class ListTaskLists {
        @Test
        @DisplayName("Should list all task lists")
        void shouldListAllTaskLists() throws Exception {
            when(taskListService.listTaskLists())
                    .thenReturn(java.util.List.of(
                            new TaskListDto(
                                    taskList1.getId(),
                                    taskList1.getTitle(),
                                    taskList1.getDescription(),
                                    0,
                                    0.0,
                                    java.util.List.of()),
                            new TaskListDto(
                                    taskList2.getId(),
                                    taskList2.getTitle(),
                                    taskList2.getDescription(),
                                    0,
                                    0.0,
                                    java.util.List.of())));

            mockMvc.perform(get("/api/v1/task-lists"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id", is(taskList1.getId().toString())))
                    .andExpect(jsonPath("$[0].title", is(taskList1.getTitle())))
                    .andExpect(jsonPath("$[1].id", is(taskList2.getId().toString())))
                    .andExpect(jsonPath("$[1].title", is(taskList2.getTitle())));
        }
    }

    @Nested
    @DisplayName("POST /api/v1/task-lists")
    class CreateTaskList {
        @Test
        @DisplayName("Should create a new task list successfully")
        void shouldCreateNewTaskListSuccessfully() throws Exception {
            var request = new CreateTaskListRequest("New Task List", "New Task List Description");

            var createdTaskListDto = new TaskListDto(
                    UUID.randomUUID(), request.title(), request.description(), 0, 0.0, java.util.List.of());

            when(taskListService.createTaskList(any(CreateTaskListRequest.class)))
                    .thenReturn(createdTaskListDto);

            mockMvc.perform(post("/api/v1/task-lists")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", is(createdTaskListDto.id().toString())))
                    .andExpect(jsonPath("$.title", is(request.title())));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/task-lists/{id}")
    class GetTaskList {
        @Test
        @DisplayName("Should get a task list by ID")
        void shouldGetTaskListById() throws Exception {
            var taskListDto = new TaskListDto(
                    taskList1.getId(), taskList1.getTitle(), taskList1.getDescription(), 0, 0.0, java.util.List.of());

            when(taskListService.getTaskList(taskList1.getId())).thenReturn(taskListDto);

            mockMvc.perform(get("/api/v1/task-lists/{id}", taskList1.getId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(taskList1.getId().toString())))
                    .andExpect(jsonPath("$.title", is(taskList1.getTitle())));
        }

        @Test
        @DisplayName("Should return 404 if task list not found")
        void shouldReturn404IfTaskListNotFound() throws Exception {
            String taskListId = UUID.randomUUID().toString();
            when(taskListService.getTaskList(any(UUID.class)))
                    .thenThrow(new TaskListNotFoundException("Task List not found with ID: " + taskListId));

            mockMvc.perform(get("/api/v1/task-lists/{id}", taskListId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail", is("Task List not found with ID: " + taskListId)));
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/task-lists/{id}")
    class UpdateTaskList {
        @Test
        @DisplayName("Should update an existing task list successfully")
        void shouldUpdateExistingTaskListSuccessfully() throws Exception {
            var request = new UpdateTaskListRequest("Updated Task List Name", "Updated Task List Description");

            var updatedTaskListDto = new TaskListDto(
                    taskList1.getId(), request.title(), request.description(), 0, 0.0, java.util.List.of());

            when(taskListService.updateTaskList(any(UUID.class), any(UpdateTaskListRequest.class)))
                    .thenReturn(updatedTaskListDto);

            mockMvc.perform(put("/api/v1/task-lists/{id}", taskList1.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(updatedTaskListDto.id().toString())))
                    .andExpect(jsonPath("$.title", is(request.title())));
        }

        @Test
        @DisplayName("Should return 404 if task list not found when updating")
        void shouldReturn404IfTaskListNotFoundWhenUpdating() throws Exception {
            String taskListId = UUID.randomUUID().toString();
            var request = new UpdateTaskListRequest("Updated Task List Name", "Updated Task List Description");

            when(taskListService.updateTaskList(any(UUID.class), any(UpdateTaskListRequest.class)))
                    .thenThrow(new TaskListNotFoundException("Task List not found with ID: " + taskListId));

            mockMvc.perform(put("/api/v1/task-lists/{id}", taskListId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail", is("Task List not found with ID: " + taskListId)));
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/task-lists/{id}")
    class DeleteTaskList {
        @Test
        @DisplayName("Should delete a task list successfully")
        void shouldDeleteTaskListSuccessfully() throws Exception {
            doNothing().when(taskListService).deleteTaskList(taskList1.getId());

            mockMvc.perform(delete("/api/v1/task-lists/{id}", taskList1.getId()))
                    .andExpect(status().isNoContent());

            when(taskListService.getTaskList(taskList1.getId()))
                    .thenThrow(new TaskListNotFoundException("Task List not found with ID: " + taskList1.getId()));

            mockMvc.perform(get("/api/v1/task-lists/{id}", taskList1.getId()))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail", is("Task List not found with ID: " + taskList1.getId())));
        }
    }
}
