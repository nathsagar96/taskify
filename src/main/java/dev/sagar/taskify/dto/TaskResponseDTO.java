package dev.sagar.taskify.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskResponseDTO {
  private Long id;
  private String title;
  private String description;
  private LocalDate dueDate;
  private Integer priority;
  private boolean completed;
  private Long taskListId;
}
