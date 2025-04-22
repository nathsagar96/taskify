package dev.sagar.taskify.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskListResponseDTO {
  private Long id;
  private String title;
  private String description;
  private double completionPercentage;
}
