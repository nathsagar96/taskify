package com.taskify.mappers;

import com.taskify.dtos.CreateTaskListRequest;
import com.taskify.dtos.TaskListDto;
import com.taskify.dtos.UpdateTaskListRequest;
import com.taskify.entities.TaskList;
import com.taskify.entities.TaskStatus;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

@Component
public class TaskListMapper {

    private final TaskMapper taskMapper;

    public TaskListMapper(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    public @Nullable TaskList fromCreateRequest(@Nullable CreateTaskListRequest request) {
        if (request == null) {
            return null;
        }

        var taskList = new TaskList();
        taskList.setTitle(request.title());
        taskList.setDescription(request.description());

        return taskList;
    }

    public @Nullable TaskList fromUpdateRequest(@Nullable UpdateTaskListRequest request) {
        if (request == null) {
            return null;
        }

        var taskList = new TaskList();
        taskList.setTitle(request.title());
        taskList.setDescription(request.description());
        return taskList;
    }

    public @Nullable TaskListDto toDto(@Nullable TaskList taskList) {
        if (taskList == null) {
            return null;
        }

        if (taskList.getTasks() == null) {
            return new TaskListDto(taskList.getId(), taskList.getTitle(), taskList.getDescription(), 0, 0.0, List.of());
        }

        var count = taskList.getTasks().size();

        var progress = taskList.getTasks().stream()
                        .filter(task -> task.getStatus() == TaskStatus.CLOSED)
                        .count()
                * 100.0
                / Math.max(count, 1);

        return new TaskListDto(
                taskList.getId(),
                taskList.getTitle(),
                taskList.getDescription(),
                count,
                progress,
                taskList.getTasks().stream().map(taskMapper::toDto).toList());
    }
}
