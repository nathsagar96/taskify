package dev.sagar.taskify.exceptions;

import dev.sagar.taskify.domain.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
  public ErrorResponse handleIllegalExceptions(RuntimeException ex, WebRequest request) {
    return new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request.getDescription(false));
  }
}
