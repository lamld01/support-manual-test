package com.lamld.supportmanualtest.server.exception;

import com.lamld.supportmanualtest.server.exception.base.BaseException;
import com.lamld.supportmanualtest.server.exception.base.ErrorCode;
import com.lamld.supportmanualtest.server.exception.base.ErrorResponse;
import com.lamld.supportmanualtest.server.exception.base.ExceptionResponse;
import com.lamld.supportmanualtest.server.utils.Constants;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = {BaseException.class})
  public ResponseEntity<ErrorResponse> badRequestException(Exception ex, HttpServletResponse response) {
    return ResponseEntity.badRequest().body(new ErrorResponse(400, ex.getMessage()));
  }
  @ExceptionHandler(value = {ForbiddenException.class})
  public ResponseEntity<ErrorResponse> forbiddenExceptionException(Exception ex, HttpServletResponse response) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(403, ex.getMessage()));
  }

  @ExceptionHandler(value = {Exception.class})
  public ResponseEntity<ErrorResponse> internalServerException(Exception ex, HttpServletResponse response) {
    log.error("=====>internalServerException: ", ex);
    return ResponseEntity.internalServerError().body(new ErrorResponse(500, Constants.INTERNAL_SERVER_ERROR));
  }

  @ExceptionHandler(value = {MethodArgumentNotValidException.class})
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
    BindingResult bindingResult = ex.getBindingResult();
    List<String> errorMessages = bindingResult.getFieldErrors().stream()
        .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
        .toList();
    return new ResponseEntity<>(ExceptionResponse.createFrom(new BaseException(StringUtils.collectionToCommaDelimitedString(errorMessages), ErrorCode.NOT_VALID)), HttpStatus.BAD_REQUEST);
  }

}