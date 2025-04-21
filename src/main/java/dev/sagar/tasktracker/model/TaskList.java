package dev.sagar.tasktracker.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskList {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  private String description;

  @OneToMany(mappedBy = "taskList", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Task> tasks = new ArrayList<>();

  public double getCompletionPercentage() {
    if (tasks.isEmpty()) {
      return 0.0;
    }

    long completedTasks = tasks.stream().filter(Task::isCompleted).count();
    return (double) completedTasks / tasks.size() * 100.0;
  }
}
