package com.epam.esm.exceptions;

import com.epam.esm.dto.errors.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomizedException.class)
  public ResponseEntity<?> handleCustomException(CustomizedException exception) {
    ErrorDTO errorResponse = new ErrorDTO(
        exception.getDescription(),
        exception.getCode().getErrorCode());
    HttpStatus status = mapErrorCodeToStatus(exception.getCode());
    return ResponseEntity.status(status).body(errorResponse);
  }

  private HttpStatus mapErrorCodeToStatus(ErrorCode errorCode) {
    switch (errorCode) {
      case TAG_DATABASE_ERROR:
        return HttpStatus.NOT_FOUND;
      case TAG_BAD_REQUEST:
        return HttpStatus.BAD_REQUEST;
      default:
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
  }
}