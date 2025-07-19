package com.taskify.common.exceptions;

import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
    ErrorResponse error = new ErrorResponse("ENTITY_NOT_FOUND", ex.getMessage(), Instant.now());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(TaskListNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleTaskListNotFound(TaskListNotFoundException ex) {
    ErrorResponse error = new ErrorResponse("TASK_LIST_NOT_FOUND", ex.getMessage(), Instant.now());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(TaskNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleTaskNotFound(TaskNotFoundException ex) {
    ErrorResponse error = new ErrorResponse("TASK_NOT_FOUND", ex.getMessage(), Instant.now());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
    Map<String, String> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

    ErrorResponse error =
        new ErrorResponse("VALIDATION_ERROR", "Invalid input parameters", Instant.now(), errors);
    return ResponseEntity.badRequest().body(error);
  }
}
