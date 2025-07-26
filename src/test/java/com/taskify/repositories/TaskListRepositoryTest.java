package com.taskify.repositories;

import static org.junit.jupiter.api.Assertions.*;

import com.taskify.BaseDataJpaTest;
import com.taskify.entities.TaskList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

class TaskListRepositoryTest extends BaseDataJpaTest {

  @Autowired private TaskListRepository taskListRepository;
  @Autowired private TestEntityManager entityManager;
  private TaskList taskList1;
  private TaskList taskList2;

  @BeforeEach
  void setUp() {
    // Clear entities managed by the TestEntityManager
    entityManager.clear();

    taskList1 = new TaskList();
    taskList1.setTitle("Personal Tasks");
    taskList1 = entityManager.persistAndFlush(taskList1);

    taskList2 = new TaskList();
    taskList2.setTitle("Shopping List");
    taskList2 = entityManager.persistAndFlush(taskList2);
  }

  @Test
  @DisplayName("Should find all task lists")
  void shouldFindAllTaskLists() {
    List<TaskList> taskLists = taskListRepository.findAll();
    assertNotNull(taskLists);
    assertEquals(2, taskLists.size());
    assertTrue(taskLists.stream().anyMatch(tl -> tl.getTitle().equals("Personal Tasks")));
    assertTrue(taskLists.stream().anyMatch(tl -> tl.getTitle().equals("Shopping List")));
  }

  @Test
  @DisplayName("Should find a task list by ID")
  void shouldFindTaskListById() {
    Optional<TaskList> foundTaskList = taskListRepository.findById(taskList1.getId());
    assertTrue(foundTaskList.isPresent());
    assertEquals(taskList1.getTitle(), foundTaskList.get().getTitle());
  }

  @Test
  @DisplayName("Should return empty optional if task list not found by ID")
  void shouldReturnEmptyOptionalIfTaskListNotFoundById() {
    Optional<TaskList> foundTaskList = taskListRepository.findById(UUID.randomUUID());
    assertTrue(foundTaskList.isEmpty());
  }

  @Test
  @DisplayName("Should delete a task list by ID")
  void shouldDeleteTaskListById() {
    taskListRepository.deleteById(taskList1.getId());
    Optional<TaskList> deletedTaskList = taskListRepository.findById(taskList1.getId());
    assertTrue(deletedTaskList.isEmpty());

    List<TaskList> remainingTaskLists = taskListRepository.findAll();
    assertEquals(1, remainingTaskLists.size());
    assertTrue(remainingTaskLists.stream().anyMatch(tl -> tl.getTitle().equals("Shopping List")));
  }

  @Test
  @DisplayName("Should not delete other task lists when deleting by ID")
  void shouldNotDeleteOtherTaskListsWhenDeletingById() {
    taskListRepository.deleteById(taskList1.getId());
    Optional<TaskList> taskList2AfterDelete = taskListRepository.findById(taskList2.getId());
    assertTrue(taskList2AfterDelete.isPresent());
  }
}
