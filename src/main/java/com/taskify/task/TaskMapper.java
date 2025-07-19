package com.taskify.task;

import com.taskify.task.domain.dtos.CreateTaskRequest;
import com.taskify.task.domain.dtos.TaskDto;
import com.taskify.task.domain.dtos.UpdateTaskRequest;
import com.taskify.task.domain.entities.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "created", ignore = true)
  @Mapping(target = "updated", ignore = true)
  @Mapping(target = "taskList", ignore = true)
  Task fromCreateRequest(CreateTaskRequest request);

  @Mapping(target = "created", ignore = true)
  @Mapping(target = "updated", ignore = true)
  @Mapping(target = "taskList", ignore = true)
  Task fromUpdateRequest(UpdateTaskRequest request);

  TaskDto toDto(Task task);
}
