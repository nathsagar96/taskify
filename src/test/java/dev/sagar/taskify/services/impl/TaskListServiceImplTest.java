package dev.sagar.taskify.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import dev.sagar.taskify.domain.entities.TaskList;
import dev.sagar.taskify.repositories.TaskListRepository;
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
class TaskListServiceImplTest {

  private final UUID taskListId = UUID.randomUUID();

  @Mock private TaskListRepository taskListRepository;

  @InjectMocks private TaskListServiceImpl taskListService;

  private TaskList taskList;

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
  }

  @Test
  void listTaskLists_ShouldReturnAllTaskLists() {
    // Arrange
    when(taskListRepository.findAll()).thenReturn(List.of(taskList));

    // Act
    var result = taskListService.listTaskLists();

    // Assert
    assertEquals(1, result.size());
    assertEquals(taskList, result.getFirst());
    verify(taskListRepository).findAll();
  }

  @Test
  void createTaskList_WithValidData_ShouldReturnSavedTaskList() {
    // Arrange
    TaskList newTaskList =
        TaskList.builder().title("New List").description("New Description").build();

    TaskList savedTaskList =
        TaskList.builder()
            .id(taskListId)
            .title(newTaskList.getTitle())
            .description(newTaskList.getDescription())
            .created(LocalDateTime.now())
            .updated(LocalDateTime.now())
            .build();

    when(taskListRepository.save(any(TaskList.class))).thenReturn(savedTaskList);

    // Act
    var result = taskListService.createTaskList(newTaskList);

    // Assert
    assertNotNull(result.getId());
    assertEquals(newTaskList.getTitle(), result.getTitle());
    assertEquals(newTaskList.getDescription(), result.getDescription());
    assertNotNull(result.getCreated());
    assertNotNull(result.getUpdated());
    verify(taskListRepository).save(any(TaskList.class));
  }

  @Test
  void createTaskList_WithExistingId_ShouldThrowException() {
    // Arrange
    TaskList invalidTaskList = TaskList.builder().id(taskListId).title("Invalid List").build();

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class, () -> taskListService.createTaskList(invalidTaskList));
    verifyNoInteractions(taskListRepository);
  }

  @Test
  void createTaskList_WithBlankTitle_ShouldThrowException() {
    // Arrange
    TaskList invalidTaskList = TaskList.builder().title("").build();

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class, () -> taskListService.createTaskList(invalidTaskList));
    verifyNoInteractions(taskListRepository);
  }

  @Test
  void getTaskList_WhenExists_ShouldReturnTaskList() {
    // Arrange
    when(taskListRepository.findById(taskListId)).thenReturn(Optional.of(taskList));

    // Act
    var result = taskListService.getTaskList(taskListId);

    // Assert
    assertTrue(result.isPresent());
    assertEquals(taskList, result.get());
    verify(taskListRepository).findById(taskListId);
  }

  @Test
  void getTaskList_WhenNotExists_ShouldReturnEmpty() {
    // Arrange
    when(taskListRepository.findById(taskListId)).thenReturn(Optional.empty());

    // Act
    var result = taskListService.getTaskList(taskListId);

    // Assert
    assertTrue(result.isEmpty());
    verify(taskListRepository).findById(taskListId);
  }

  @Test
  void updateTaskList_WithValidData_ShouldReturnUpdatedTaskList() {
    // Arrange
    TaskList updatedTaskList =
        TaskList.builder()
            .id(taskListId)
            .title("Updated List")
            .description("Updated Description")
            .build();

    when(taskListRepository.findById(taskListId)).thenReturn(Optional.of(taskList));
    when(taskListRepository.save(any(TaskList.class))).thenReturn(updatedTaskList);

    // Act
    var result = taskListService.updateTaskList(taskListId, updatedTaskList);

    // Assert
    assertEquals(updatedTaskList.getTitle(), result.getTitle());
    assertEquals(updatedTaskList.getDescription(), result.getDescription());
    verify(taskListRepository).findById(taskListId);
    verify(taskListRepository).save(any(TaskList.class));
  }

  @Test
  void updateTaskList_WithNullId_ShouldThrowException() {
    // Arrange
    TaskList invalidTaskList = TaskList.builder().title("Invalid List").build();

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> taskListService.updateTaskList(taskListId, invalidTaskList));
    verifyNoInteractions(taskListRepository);
  }

  @Test
  void updateTaskList_WithMismatchedId_ShouldThrowException() {
    // Arrange
    TaskList invalidTaskList =
        TaskList.builder().id(UUID.randomUUID()).title("Invalid List").build();

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> taskListService.updateTaskList(taskListId, invalidTaskList));
    verifyNoInteractions(taskListRepository);
  }

  @Test
  void updateTaskList_WhenNotExists_ShouldThrowException() {
    // Arrange
    when(taskListRepository.findById(taskListId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        IllegalStateException.class, () -> taskListService.updateTaskList(taskListId, taskList));
    verify(taskListRepository).findById(taskListId);
    verify(taskListRepository, never()).save(any());
  }

  @Test
  void deleteTaskList_ShouldCallRepository() {
    // Arrange - nothing to arrange for void method

    // Act
    taskListService.deleteTaskList(taskListId);

    // Assert
    verify(taskListRepository).deleteById(taskListId);
  }
}
