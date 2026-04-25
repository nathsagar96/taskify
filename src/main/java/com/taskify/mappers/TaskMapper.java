package com.taskify.mappers;

import com.taskify.dtos.CreateTaskRequest;
import com.taskify.dtos.TaskDto;
import com.taskify.dtos.UpdateTaskRequest;
import com.taskify.entities.Task;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public @Nullable Task fromCreateRequest(@Nullable CreateTaskRequest request) {
        if (request == null) {
            return null;
        }

        var task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setDueDate(request.dueDate());
        task.setPriority(request.priority());

        return task;
    }

    public @Nullable Task fromUpdateRequest(@Nullable UpdateTaskRequest request) {
        if (request == null) {
            return null;
        }

        var task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setDueDate(request.dueDate());
        task.setPriority(request.priority());
        task.setStatus(request.status());

        return task;
    }

    public @Nullable TaskDto toDto(@Nullable Task task) {
        if (task == null) {
            return null;
        }

        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getPriority(),
                task.getStatus());
    }
}
