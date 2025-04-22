package dev.sagar.taskify.services.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import dev.sagar.taskify.domain.entities.Task;
import dev.sagar.taskify.domain.entities.TaskList;
import dev.sagar.taskify.domain.entities.TaskPriority;
import dev.sagar.taskify.domain.entities.TaskStatus;
import dev.sagar.taskify.repositories.TaskListRepository;
import dev.sagar.taskify.repositories.TaskRepository;
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
class TaskServiceImplTest {

    @Mock
    private TaskListRepository taskListRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private UUID taskListId;
    private UUID taskId;
    private Task task;
    private TaskList taskList;

    @BeforeEach
    void setUp() {
        taskListId = UUID.randomUUID();
        taskId = UUID.randomUUID();

        taskList = TaskList.builder()
                .id(taskListId)
                .title("Test List")
                .build();

        task = Task.builder()
                .id(taskId)
                .title("Test Task")
                .description("Test Description")
                .dueDate(LocalDateTime.now().plusDays(1))
                .priority(TaskPriority.MEDIUM)
                .status(TaskStatus.OPEN)
                .taskList(taskList)
                .build();
    }

    @Test
    void listTasks_ShouldReturnTasksForList() {
        // Arrange
        when(taskRepository.findByTaskListId(taskListId)).thenReturn(List.of(task));

        // Act
        var result = taskService.listTasks(taskListId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(task, result.get(0));
        verify(taskRepository).findByTaskListId(taskListId);
    }

    @Test
    void createTask_WithValidData_ShouldReturnSavedTask() {
        // Arrange
        Task newTask = Task.builder()
                .title("New Task")
                .description("New Description")
                .build();

        when(taskListRepository.findById(taskListId)).thenReturn(Optional.of(taskList));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task t = invocation.getArgument(0);
            t.setId(taskId);
            return t;
        });

        // Act
        var result = taskService.createTask(taskListId, newTask);

        // Assert
        assertNotNull(result.getId());
        assertEquals("New Task", result.getTitle());
        assertEquals("New Description", result.getDescription());
        assertEquals(TaskPriority.MEDIUM, result.getPriority()); // Default
        assertEquals(TaskStatus.OPEN, result.getStatus());
        assertNotNull(result.getCreated());
        assertNotNull(result.getUpdated());
        assertEquals(taskList, result.getTaskList());
        verify(taskListRepository).findById(taskListId);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void createTask_WithExistingId_ShouldThrowException() {
        // Arrange
        Task invalidTask = Task.builder()
                .id(taskId)
                .title("Invalid Task")
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> taskService.createTask(taskListId, invalidTask));
        verifyNoInteractions(taskRepository);
    }

    @Test
    void createTask_WithBlankTitle_ShouldThrowException() {
        // Arrange
        Task invalidTask = Task.builder()
                .title("")
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> taskService.createTask(taskListId, invalidTask));
        verifyNoInteractions(taskRepository);
    }

    @Test
    void createTask_WithInvalidTaskList_ShouldThrowException() {
        // Arrange
        Task newTask = Task.builder()
                .title("New Task")
                .build();

        when(taskListRepository.findById(taskListId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> taskService.createTask(taskListId, newTask));
        verify(taskListRepository).findById(taskListId);
        verifyNoInteractions(taskRepository);
    }

    @Test
    void getTask_WhenExists_ShouldReturnTask() {
        // Arrange
        when(taskRepository.findByTaskListIdAndId(taskListId, taskId)).thenReturn(Optional.of(task));

        // Act
        var result = taskService.getTask(taskListId, taskId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(task, result.get());
        verify(taskRepository).findByTaskListIdAndId(taskListId, taskId);
    }

    @Test
    void getTask_WhenNotExists_ShouldReturnEmpty() {
        // Arrange
        when(taskRepository.findByTaskListIdAndId(taskListId, taskId)).thenReturn(Optional.empty());

        // Act
        var result = taskService.getTask(taskListId, taskId);

        // Assert
        assertTrue(result.isEmpty());
        verify(taskRepository).findByTaskListIdAndId(taskListId, taskId);
    }

    @Test
    void updateTask_WithValidData_ShouldReturnUpdatedTask() {
        // Arrange
        Task updatedTask = Task.builder()
                .id(taskId)
                .title("Updated Task")
                .description("Updated Description")
                .dueDate(LocalDateTime.now().plusDays(2))
                .priority(TaskPriority.HIGH)
                .status(TaskStatus.CLOSED)
                .build();

        when(taskRepository.findByTaskListIdAndId(taskListId, taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        // Act
        var result = taskService.updateTask(taskListId, taskId, updatedTask);

        // Assert
        assertEquals("Updated Task", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(TaskPriority.HIGH, result.getPriority());
        assertEquals(TaskStatus.CLOSED, result.getStatus());
        verify(taskRepository).findByTaskListIdAndId(taskListId, taskId);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void updateTask_WithNullId_ShouldThrowException() {
        // Arrange
        Task invalidTask = Task.builder()
                .title("Invalid Task")
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> taskService.updateTask(taskListId, taskId, invalidTask));
        verifyNoInteractions(taskRepository);
    }

    @Test
    void updateTask_WithMismatchedId_ShouldThrowException() {
        // Arrange
        Task invalidTask = Task.builder()
                .id(UUID.randomUUID())
                .title("Invalid Task")
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> taskService.updateTask(taskListId, taskId, invalidTask));
        verifyNoInteractions(taskRepository);
    }

    @Test
    void updateTask_WithNullPriority_ShouldThrowException() {
        // Arrange
        Task invalidTask = Task.builder()
                .id(taskId)
                .title("Invalid Task")
                .priority(null)
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> taskService.updateTask(taskListId, taskId, invalidTask));
        verifyNoInteractions(taskRepository);
    }

    @Test
    void updateTask_WithNullStatus_ShouldThrowException() {
        // Arrange
        Task invalidTask = Task.builder()
                .id(taskId)
                .title("Invalid Task")
                .status(null)
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> taskService.updateTask(taskListId, taskId, invalidTask));
        verifyNoInteractions(taskRepository);
    }

    @Test
    void updateTask_WhenNotExists_ShouldThrowException() {
        // Arrange
        Task updatedTask = Task.builder()
                .id(taskId)
                .title("Updated Task")
                .priority(TaskPriority.MEDIUM)
                .status(TaskStatus.OPEN)
                .build();

        when(taskRepository.findByTaskListIdAndId(taskListId, taskId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> taskService.updateTask(taskListId, taskId, updatedTask));
        verify(taskRepository).findByTaskListIdAndId(taskListId, taskId);
        verify(taskRepository, never()).save(any());
    }

    @Test
    void deleteTask_ShouldCallRepository() {
        // Act
        taskService.deleteTask(taskListId, taskId);

        // Assert
        verify(taskRepository).deleteByTaskListIdAndId(taskListId, taskId);
    }
}