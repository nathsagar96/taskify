package dev.sagar.taskify.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskListDTO {
  @NotBlank(message = "Title cannot be blank")
  private String title;

  @NotBlank(message = "Description cannot be blank")
  private String description;
}
