package dev.sagar.taskify.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import dev.sagar.taskify.domain.dto.TaskListDto;
import dev.sagar.taskify.domain.entities.TaskList;
import dev.sagar.taskify.mappers.TaskListMapper;
import dev.sagar.taskify.services.TaskListService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskListControllerTest {

  private final UUID taskListId = UUID.randomUUID();

  @Mock private TaskListService taskListService;

  @Mock private TaskListMapper taskListMapper;

  @InjectMocks private TaskListController taskListController;

  private TaskList taskList;
  private TaskListDto taskListDto;

  @BeforeEach
  void setUp() {
    taskList =
        TaskList.builder()
            .id(taskListId)
            .title("Test List")
            .description("Test Description")
            .created(LocalDateTime.now())
            .updated(LocalDateTime.now())
            .build();

    taskListDto = new TaskListDto(taskListId, "Test List", "Test Description", 0, 0.0, null);
  }

  @Test
  void listTaskLists_ShouldReturnListOfTaskLists() {
    // Arrange
    when(taskListService.listTaskLists()).thenReturn(List.of(taskList));
    when(taskListMapper.toDto(taskList)).thenReturn(taskListDto);

    // Act
    var result = taskListController.listTaskLists();

    // Assert
    assertEquals(1, result.size());
    assertEquals(taskListDto, result.getFirst());
    verify(taskListService).listTaskLists();
    verify(taskListMapper).toDto(taskList);
  }

  @Test
  void createTaskList_ShouldReturnCreatedTaskList() {
    // Arrange
    when(taskListMapper.fromDto(taskListDto)).thenReturn(taskList);
    when(taskListService.createTaskList(taskList)).thenReturn(taskList);
    when(taskListMapper.toDto(taskList)).thenReturn(taskListDto);

    // Act
    var result = taskListController.createTaskList(taskListDto);

    // Assert
    assertEquals(taskListDto, result);
    verify(taskListMapper).fromDto(taskListDto);
    verify(taskListService).createTaskList(taskList);
    verify(taskListMapper).toDto(taskList);
  }

  @Test
  void getTaskList_WhenExists_ShouldReturnTaskList() {
    // Arrange
    when(taskListService.getTaskList(taskListId)).thenReturn(Optional.of(taskList));
    when(taskListMapper.toDto(taskList)).thenReturn(taskListDto);

    // Act
    var result = taskListController.getTaskList(taskListId);

    // Assert
    assertTrue(result.isPresent());
    assertEquals(taskListDto, result.get());
    verify(taskListService).getTaskList(taskListId);
    verify(taskListMapper).toDto(taskList);
  }

  @Test
  void getTaskList_WhenNotExists_ShouldReturnEmpty() {
    // Arrange
    when(taskListService.getTaskList(taskListId)).thenReturn(Optional.empty());

    // Act
    var result = taskListController.getTaskList(taskListId);

    // Assert
    assertTrue(result.isEmpty());
    verify(taskListService).getTaskList(taskListId);
    verifyNoInteractions(taskListMapper);
  }

  @Test
  void updateTaskList_ShouldReturnUpdatedTaskList() {
    // Arrange
    when(taskListMapper.fromDto(taskListDto)).thenReturn(taskList);
    when(taskListService.updateTaskList(taskListId, taskList)).thenReturn(taskList);
    when(taskListMapper.toDto(taskList)).thenReturn(taskListDto);

    // Act
    var result = taskListController.updateTaskList(taskListId, taskListDto);

    // Assert
    assertEquals(taskListDto, result);
    verify(taskListMapper).fromDto(taskListDto);
    verify(taskListService).updateTaskList(taskListId, taskList);
    verify(taskListMapper).toDto(taskList);
  }

  @Test
  void deleteTaskList_ShouldCallService() {
    // Arrange - nothing to arrange for void method

    // Act
    taskListController.deleteTaskList(taskListId);

    // Assert
    verify(taskListService).deleteTaskList(taskListId);
  }
}
