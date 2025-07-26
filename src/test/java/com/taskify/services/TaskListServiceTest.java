package com.taskify.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.taskify.entities.TaskList;
import com.taskify.exceptions.TaskListNotFoundException;
import com.taskify.repositories.TaskListRepository;
import com.taskify.repositories.TaskRepository;
import com.taskify.services.impl.TaskListServiceImpl;
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
class TaskListServiceTest {

  @Mock private TaskListRepository taskListRepository;

  @Mock private TaskRepository taskRepository;

  @InjectMocks private TaskListServiceImpl taskListService;

  private UUID taskListId;
  private TaskList taskList;

  @BeforeEach
  void setUp() {
    taskListId = UUID.randomUUID();
    taskList = new TaskList();
    taskList.setId(taskListId);
    taskList.setTitle("Test Task List");
  }

  @Test
  @DisplayName("Should create a task list successfully")
  void shouldCreateTaskListSuccessfully() {
    when(taskListRepository.save(any(TaskList.class))).thenReturn(taskList);

    TaskList createdTaskList = taskListService.createTaskList(taskList);

    assertNotNull(createdTaskList);
    assertEquals(taskList.getTitle(), createdTaskList.getTitle());
    verify(taskListRepository, times(1)).save(taskList);
  }

  @Test
  @DisplayName("Should list all task lists")
  void shouldListAllTaskLists() {
    when(taskListRepository.findAll()).thenReturn(List.of(taskList));

    List<TaskList> taskLists = taskListService.listTaskLists();

    assertNotNull(taskLists);
    assertFalse(taskLists.isEmpty());
    assertEquals(1, taskLists.size());
    assertEquals(taskList.getTitle(), taskLists.get(0).getTitle());
    verify(taskListRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("Should return empty list when no task lists found")
  void shouldReturnEmptyListWhenNoTaskListsFound() {
    when(taskListRepository.findAll()).thenReturn(Collections.emptyList());

    List<TaskList> taskLists = taskListService.listTaskLists();

    assertNotNull(taskLists);
    assertTrue(taskLists.isEmpty());
    verify(taskListRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("Should get a task list by ID")
  void shouldGetTaskListById() {
    when(taskListRepository.findById(taskListId)).thenReturn(Optional.of(taskList));

    Optional<TaskList> foundTaskList = taskListService.getTaskList(taskListId);

    assertTrue(foundTaskList.isPresent());
    assertEquals(taskList.getTitle(), foundTaskList.get().getTitle());
    verify(taskListRepository, times(1)).findById(taskListId);
  }

  @Test
  @DisplayName("Should return empty optional when task list not found by ID")
  void shouldReturnEmptyOptionalWhenTaskListNotFoundById() {
    when(taskListRepository.findById(taskListId)).thenReturn(Optional.empty());

    Optional<TaskList> foundTaskList = taskListService.getTaskList(taskListId);

    assertTrue(foundTaskList.isEmpty());
    verify(taskListRepository, times(1)).findById(taskListId);
  }

  @Test
  @DisplayName("Should update a task list successfully")
  void shouldUpdateTaskListSuccessfully() {
    TaskList updatedTaskListDetails = new TaskList();
    updatedTaskListDetails.setTitle("Updated Task List Name");

    when(taskListRepository.findById(taskListId)).thenReturn(Optional.of(taskList));
    when(taskListRepository.save(any(TaskList.class))).thenReturn(taskList);

    TaskList result = taskListService.updateTaskList(taskListId, updatedTaskListDetails);

    assertNotNull(result);
    assertEquals(updatedTaskListDetails.getTitle(), result.getTitle());
    verify(taskListRepository, times(1)).findById(taskListId);
    verify(taskListRepository, times(1)).save(taskList);
  }

  @Test
  @DisplayName("Should throw TaskListNotFoundException when updating non-existent task list")
  void shouldThrowTaskListNotFoundExceptionWhenUpdatingNonExistentTaskList() {
    TaskList updatedTaskListDetails = new TaskList();
    when(taskListRepository.findById(taskListId)).thenReturn(Optional.empty());

    assertThrows(
        TaskListNotFoundException.class,
        () -> taskListService.updateTaskList(taskListId, updatedTaskListDetails));
    verify(taskListRepository, times(1)).findById(taskListId);
    verify(taskListRepository, never()).save(any(TaskList.class));
  }

  @Test
  @DisplayName("Should delete a task list successfully")
  void shouldDeleteTaskListSuccessfully() {
    doNothing().when(taskListRepository).deleteById(taskListId);

    taskListService.deleteTaskList(taskListId);

    verify(taskListRepository, times(1)).deleteById(taskListId);
  }
}
