package com.taskify.mappers;

import static org.junit.jupiter.api.Assertions.*;

import com.taskify.dtos.CreateTaskListRequest;
import com.taskify.dtos.TaskListDto;
import com.taskify.dtos.UpdateTaskListRequest;
import com.taskify.entities.TaskList;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class TaskListMapperTest {

  private TaskListMapper taskListMapper;

  @BeforeEach
  void setUp() {
    taskListMapper = Mappers.getMapper(TaskListMapper.class);
  }

  @Test
  @DisplayName("Should map TaskList entity to TaskListDto")
  void shouldMapTaskListEntityToTaskListDto() {
    UUID taskListId = UUID.randomUUID();
    TaskList taskList = new TaskList();
    taskList.setId(taskListId);
    taskList.setTitle("Test Task List");

    TaskListDto taskListDto = taskListMapper.toDto(taskList);

    assertNotNull(taskListDto);
    assertEquals(taskList.getId(), taskListDto.id());
    assertEquals(taskList.getTitle(), taskListDto.title());
  }

  @Test
  @DisplayName("Should map CreateTaskListRequest to TaskList entity")
  void shouldMapCreateTaskListRequestToTaskListEntity() {
    CreateTaskListRequest request =
        new CreateTaskListRequest("New Task List", "New Task List Description");

    TaskList taskList = taskListMapper.fromCreateRequest(request);

    assertNotNull(taskList);
    assertEquals(request.title(), taskList.getTitle());
    assertNull(taskList.getId()); // ID should be null for new entity
  }

  @Test
  @DisplayName("Should map UpdateTaskListRequest to TaskList entity")
  void shouldMapUpdateTaskListRequestToTaskListEntity() {
    UpdateTaskListRequest request =
        new UpdateTaskListRequest("Updated Task List", "Updated Task List Description");

    TaskList taskList = taskListMapper.fromUpdateRequest(request);

    assertNotNull(taskList);
    assertEquals(request.title(), taskList.getTitle());
    assertNull(taskList.getId()); // ID should be null for mapping
  }
}