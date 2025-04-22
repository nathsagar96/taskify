package dev.sagar.taskify.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import dev.sagar.taskify.dto.TaskListDTO;
import dev.sagar.taskify.dto.TaskListResponseDTO;
import dev.sagar.taskify.service.TaskListService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskListControllerTest {

  @Mock
  private TaskListService taskListService;

  @InjectMocks
  private TaskListController taskListController;

  private TaskListDTO taskListDTO;
  private TaskListResponseDTO responseDTO;

  @BeforeEach
  void setUp() {
    taskListDTO = TaskListDTO.builder()
            .title("Test List")
            .description("Test Description")
            .build();

    responseDTO = TaskListResponseDTO.builder()
            .id(1L)
            .title("Test List")
            .description("Test Description")
            .completionPercentage(0.0)
            .build();
  }

  @Test
  void createTaskList_ShouldReturnTaskListResponseDTO() {
    when(taskListService.createTaskList(any(TaskListDTO.class))).thenReturn(responseDTO);

    TaskListResponseDTO result = taskListController.createTaskList(taskListDTO);

    assertEquals(responseDTO, result);
    verify(taskListService, times(1)).createTaskList(taskListDTO);
  }

  @Test
  void getAllTaskLists_ShouldReturnListOfTaskLists() {
    List<TaskListResponseDTO> taskLists = Collections.singletonList(responseDTO);
    when(taskListService.getAllTaskLists()).thenReturn(taskLists);

    List<TaskListResponseDTO> result = taskListController.getAllTaskLists();

    assertEquals(taskLists, result);
    verify(taskListService, times(1)).getAllTaskLists();
  }

  @Test
  void getTaskListById_ShouldReturnTaskList() {
    when(taskListService.getTaskListById(anyLong())).thenReturn(responseDTO);

    TaskListResponseDTO result = taskListController.getTaskListById(1L);

    assertEquals(responseDTO, result);
    verify(taskListService, times(1)).getTaskListById(1L);
  }

  @Test
  void updateTaskList_ShouldReturnUpdatedTaskList() {
    when(taskListService.updateTaskList(anyLong(), any(TaskListDTO.class))).thenReturn(responseDTO);

    TaskListResponseDTO result = taskListController.updateTaskList(1L, taskListDTO);

    assertEquals(responseDTO, result);
    verify(taskListService, times(1)).updateTaskList(1L, taskListDTO);
  }

  @Test
  void deleteTaskList_ShouldReturnVoid() {
    doNothing().when(taskListService).deleteTaskList(anyLong());

    taskListController.deleteTaskList(1L);

    verify(taskListService, times(1)).deleteTaskList(1L);
  }
}