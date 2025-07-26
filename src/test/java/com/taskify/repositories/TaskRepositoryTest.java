package com.taskify.repositories;

import static org.junit.jupiter.api.Assertions.*;

import com.taskify.BaseDataJpaTest;
import com.taskify.entities.Task;
import com.taskify.entities.TaskList;
import com.taskify.entities.TaskPriority;
import com.taskify.entities.TaskStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

class TaskRepositoryTest extends BaseDataJpaTest {

  @Autowired private TaskRepository taskRepository;
  @Autowired private TestEntityManager entityManager;
  private TaskList taskList;
  private Task task1;
  private Task task2;

  @BeforeEach
  void setUp() {
    // Clear entities managed by the TestEntityManager
    entityManager.clear();

    taskList = new TaskList();
    taskList.setTitle("Work Tasks");
    taskList = entityManager.persistAndFlush(taskList);

    task1 = new Task();
    task1.setTitle("Complete Project Proposal");
    task1.setDescription("Write and finalize the project proposal document.");
    task1.setDueDate(LocalDateTime.now().plusDays(5));
    task1.setPriority(TaskPriority.HIGH);
    task1.setStatus(TaskStatus.OPEN);
    task1.setTaskList(taskList);
    task1 = entityManager.persistAndFlush(task1);

    task2 = new Task();
    task2.setTitle("Schedule Team Meeting");
    task2.setDescription("Arrange a meeting with the team to discuss progress.");
    task2.setDueDate(LocalDateTime.now().plusDays(2));
    task2.setPriority(TaskPriority.MEDIUM);
    task2.setStatus(TaskStatus.OPEN);
    task2.setTaskList(taskList);
    task2 = entityManager.persistAndFlush(task2);
  }

  @Test
  @DisplayName("Should find tasks by task list ID")
  void shouldFindByTaskListId() {
    List<Task> tasks = taskRepository.findByTaskListId(taskList.getId());
    assertNotNull(tasks);
    assertEquals(2, tasks.size());
    assertTrue(tasks.stream().anyMatch(t -> t.getTitle().equals("Complete Project Proposal")));
    assertTrue(tasks.stream().anyMatch(t -> t.getTitle().equals("Schedule Team Meeting")));
  }

  @Test
  @DisplayName("Should find a task by task list ID and task ID")
  void shouldFindByTaskListIdAndId() {
    Optional<Task> foundTask =
        taskRepository.findByTaskListIdAndId(taskList.getId(), task1.getId());
    assertTrue(foundTask.isPresent());
    assertEquals(task1.getTitle(), foundTask.get().getTitle());
  }

  @Test
  @DisplayName("Should return empty optional if task not found by task list ID and task ID")
  void shouldReturnEmptyOptionalIfTaskNotFoundByTaskListIdAndId() {
    Optional<Task> foundTask =
        taskRepository.findByTaskListIdAndId(taskList.getId(), UUID.randomUUID());
    assertTrue(foundTask.isEmpty());
  }

  @Test
  @DisplayName("Should delete a task by task list ID and task ID")
  void shouldDeleteByTaskListIdAndId() {
    taskRepository.deleteByTaskListIdAndId(taskList.getId(), task1.getId());
    Optional<Task> deletedTask = taskRepository.findById(task1.getId());
    assertTrue(deletedTask.isEmpty());

    List<Task> remainingTasks = taskRepository.findByTaskListId(taskList.getId());
    assertEquals(1, remainingTasks.size());
    assertTrue(remainingTasks.stream().anyMatch(t -> t.getTitle().equals("Schedule Team Meeting")));
  }

  @Test
  @DisplayName("Should not delete other tasks when deleting by task list ID and task ID")
  void shouldNotDeleteOtherTasksWhenDeletingByTaskListIdAndId() {
    taskRepository.deleteByTaskListIdAndId(taskList.getId(), task1.getId());
    Optional<Task> task2AfterDelete = taskRepository.findById(task2.getId());
    assertTrue(task2AfterDelete.isPresent());
  }
}
