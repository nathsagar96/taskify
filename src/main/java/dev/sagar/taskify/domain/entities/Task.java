package dev.sagar.taskify.domain.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "description")
  private String description;

  @Column(name = "due_date")
  private LocalDateTime dueDate;

  @Column(name = "priority", nullable = false)
  private TaskPriority priority;

  @Column(name = "status", nullable = false)
  private TaskStatus status;

  @Column(name = "created", nullable = false)
  private LocalDateTime created;

  @Column(name = "updated", nullable = false)
  private LocalDateTime updated;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "task_list_id")
  private TaskList taskList;
}
