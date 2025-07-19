package com.taskify.common.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class TaskNotFoundException extends EntityNotFoundException {
  public TaskNotFoundException(String message) {
    super(message);
  }
}
