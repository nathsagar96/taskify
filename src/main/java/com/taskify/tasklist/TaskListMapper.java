package com.taskify.tasklist;

import com.taskify.tasklist.domain.dtos.CreateTaskListRequest;
import com.taskify.tasklist.domain.dtos.TaskListDto;
import com.taskify.tasklist.domain.dtos.UpdateTaskListRequest;
import com.taskify.tasklist.domain.entities.TaskList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskListMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "created", ignore = true)
  @Mapping(target = "updated", ignore = true)
  @Mapping(target = "tasks", ignore = true)
  TaskList fromCreateRequest(CreateTaskListRequest request);

  @Mapping(target = "created", ignore = true)
  @Mapping(target = "updated", ignore = true)
  @Mapping(target = "tasks", ignore = true)
  TaskList fromUpdateRequest(UpdateTaskListRequest request);

  TaskListDto toDto(TaskList taskList);
}
