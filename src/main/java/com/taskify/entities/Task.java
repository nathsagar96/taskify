package com.taskify.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "tasks")
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false, nullable = false)
  private UUID id;

  @Column(nullable = false)
  private String title;

  private String description;
  private LocalDateTime dueDate;

  @Column(nullable = false)
  private TaskPriority priority;

  @Column(nullable = false)
  private TaskStatus status;

  @CreationTimestamp
  @Column(nullable = false)
  private LocalDateTime created;

  @UpdateTimestamp private LocalDateTime updated;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "task_list_id")
  private TaskList taskList;

  public Task() {}

  public Task(
      UUID id,
      String title,
      String description,
      LocalDateTime dueDate,
      TaskPriority priority,
      TaskStatus status,
      LocalDateTime created,
      LocalDateTime updated,
      TaskList taskList) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.dueDate = dueDate;
    this.priority = priority;
    this.status = status;
    this.created = created;
    this.updated = updated;
    this.taskList = taskList;
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

  public LocalDateTime getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDateTime dueDate) {
    this.dueDate = dueDate;
  }

  public TaskPriority getPriority() {
    return priority;
  }

  public void setPriority(TaskPriority priority) {
    this.priority = priority;
  }

  public TaskStatus getStatus() {
    return status;
  }

  public void setStatus(TaskStatus status) {
    this.status = status;
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

  public TaskList getTaskList() {
    return taskList;
  }

  public void setTaskList(TaskList taskList) {
    this.taskList = taskList;
  }
}
