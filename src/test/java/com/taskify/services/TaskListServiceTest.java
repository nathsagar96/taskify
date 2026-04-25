package com.taskify.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.taskify.dtos.CreateTaskListRequest;
import com.taskify.dtos.TaskListDto;
import com.taskify.dtos.UpdateTaskListRequest;
import com.taskify.entities.TaskList;
import com.taskify.exceptions.TaskListNotFoundException;
import com.taskify.mappers.TaskListMapper;
import com.taskify.repositories.TaskListRepository;
import com.taskify.repositories.TaskRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskListService - Service Layer Tests")
class TaskListServiceTest {

    @Mock
    private TaskListRepository taskListRepository;

    @Mock
    private TaskListMapper taskListMapper;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskListService taskListService;

    private UUID taskListId;
    private TaskList taskList;
    private TaskListDto taskListDto;

    @BeforeEach
    void setUp() {
        taskListId = UUID.randomUUID();
        taskList = new TaskList();
        taskList.setId(taskListId);
        taskList.setTitle("Test Task List");

        taskListDto = new TaskListDto(taskListId, taskList.getTitle(), taskList.getDescription(), 0, 0.0, List.of());
    }

    @Nested
    @DisplayName("Create Task List Operations")
    class CreateTaskListOperations {
        @Test
        @DisplayName("Should create a task list successfully")
        void shouldCreateTaskListSuccessfully() {
            var request = new CreateTaskListRequest("Test Task List", "Description");

            when(taskListMapper.fromCreateRequest(any(CreateTaskListRequest.class)))
                    .thenReturn(taskList);
            when(taskListRepository.save(any(TaskList.class))).thenReturn(taskList);
            when(taskListMapper.toDto(any(TaskList.class))).thenReturn(taskListDto);

            TaskListDto createdTaskList = taskListService.createTaskList(request);

            assertNotNull(createdTaskList);
            assertEquals(taskListDto.title(), createdTaskList.title());
            verify(taskListRepository, times(1)).save(taskList);
        }
    }

    @Nested
    @DisplayName("List Task Lists Operations")
    class ListTaskListsOperations {
        @Test
        @DisplayName("Should list all task lists")
        void shouldListAllTaskLists() {
            when(taskListRepository.findAll()).thenReturn(List.of(taskList));
            when(taskListMapper.toDto(any(TaskList.class))).thenReturn(taskListDto);

            List<TaskListDto> taskLists = taskListService.listTaskLists();

            assertNotNull(taskLists);
            assertFalse(taskLists.isEmpty());
            assertEquals(1, taskLists.size());
            assertEquals(taskListDto.title(), taskLists.getFirst().title());
            verify(taskListRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no task lists found")
        void shouldReturnEmptyListWhenNoTaskListsFound() {
            when(taskListRepository.findAll()).thenReturn(List.of());

            List<TaskListDto> taskLists = taskListService.listTaskLists();

            assertNotNull(taskLists);
            assertTrue(taskLists.isEmpty());
            verify(taskListRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Get Task List Operations")
    class GetTaskListOperations {
        @Test
        @DisplayName("Should get a task list by ID")
        void shouldGetTaskListById() {
            when(taskListRepository.findById(taskListId)).thenReturn(Optional.of(taskList));
            when(taskListMapper.toDto(any(TaskList.class))).thenReturn(taskListDto);

            TaskListDto foundTaskList = taskListService.getTaskList(taskListId);

            assertNotNull(foundTaskList);
            assertEquals(taskListDto.title(), foundTaskList.title());
            verify(taskListRepository, times(1)).findById(taskListId);
        }

        @Test
        @DisplayName("Should throw TaskListNotFoundException when task list not found by ID")
        void shouldThrowTaskListNotFoundExceptionWhenTaskListNotFoundById() {
            when(taskListRepository.findById(taskListId)).thenReturn(Optional.empty());

            assertThrows(TaskListNotFoundException.class, () -> taskListService.getTaskList(taskListId));
            verify(taskListRepository, times(1)).findById(taskListId);
        }
    }

    @Nested
    @DisplayName("Update Task List Operations")
    class UpdateTaskListOperations {
        @Test
        @DisplayName("Should update a task list successfully")
        void shouldUpdateTaskListSuccessfully() {
            var request = new UpdateTaskListRequest("Updated Task List Name", "Updated Description");
            var updatedTaskListDto =
                    new TaskListDto(taskListId, request.title(), request.description(), 0, 0.0, List.of());

            when(taskListRepository.findById(taskListId)).thenReturn(Optional.of(taskList));
            when(taskListMapper.fromUpdateRequest(any(UpdateTaskListRequest.class)))
                    .thenReturn(taskList);
            when(taskListRepository.save(any(TaskList.class))).thenReturn(taskList);
            when(taskListMapper.toDto(any(TaskList.class))).thenReturn(updatedTaskListDto);

            TaskListDto result = taskListService.updateTaskList(taskListId, request);

            assertNotNull(result);
            assertEquals(updatedTaskListDto.title(), result.title());
            verify(taskListRepository, times(1)).findById(taskListId);
            verify(taskListRepository, times(1)).save(taskList);
        }

        @Test
        @DisplayName("Should throw TaskListNotFoundException when updating non-existent task list")
        void shouldThrowTaskListNotFoundExceptionWhenUpdatingNonExistentTaskList() {
            var request = new UpdateTaskListRequest("Updated Task List Name", "Updated Description");
            when(taskListRepository.findById(taskListId)).thenReturn(Optional.empty());

            assertThrows(TaskListNotFoundException.class, () -> taskListService.updateTaskList(taskListId, request));
            verify(taskListRepository, times(1)).findById(taskListId);
            verify(taskListRepository, never()).save(any(TaskList.class));
        }
    }

    @Nested
    @DisplayName("Delete Task List Operations")
    class DeleteTaskListOperations {
        @Test
        @DisplayName("Should delete a task list successfully")
        void shouldDeleteTaskListSuccessfully() {
            doNothing().when(taskListRepository).deleteById(taskListId);

            taskListService.deleteTaskList(taskListId);

            verify(taskListRepository, times(1)).deleteById(taskListId);
        }
    }
}
