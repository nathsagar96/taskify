package com.taskify.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class TaskListNotFoundException extends EntityNotFoundException {
  public TaskListNotFoundException(String message) {
    super(message);
  }
}
