package com.taskify.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "task_lists")
public class TaskList {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false, nullable = false)
  private UUID id;

  @Column(nullable = false)
  private String title;

  private String description;

  @CreationTimestamp
  @Column(nullable = false)
  private LocalDateTime created;

  @UpdateTimestamp private LocalDateTime updated;

  @OneToMany(
      mappedBy = "taskList",
      cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
  private List<Task> tasks;

  public TaskList() {}

  public TaskList(
      UUID id,
      String title,
      String description,
      LocalDateTime created,
      LocalDateTime updated,
      List<Task> tasks) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.created = created;
    this.updated = updated;
    this.tasks = tasks;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDateTime getCreated() {
    return created;
  }

  public void setCreated(LocalDateTime created) {
    this.created = created;
  }

  public LocalDateTime getUpdated() {
    return updated;
  }

  public void setUpdated(LocalDateTime updated) {
    this.updated = updated;
  }

  public List<Task> getTasks() {
    return tasks;
  }

  public void setTasks(List<Task> tasks) {
    this.tasks = tasks;
  }
}
