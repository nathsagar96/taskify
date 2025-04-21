package dev.sagar.tasktracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskDTO {
  @NotBlank(message = "Title cannot be blank")
  private String title;

  @NotBlank(message = "Description cannot be blank")
  private String description;

  @NotNull(message = "Due date cannot be null")
  private LocalDate dueDate;

  @NotNull(message = "Priority cannot be null")
  private Integer priority;

  @NotNull(message = "Completed status cannot be null")
  private Boolean completed;
}
