package com.lamld.supportmanualtest.server.exception;

import com.lamld.supportmanualtest.server.exception.base.BaseException;
import com.lamld.supportmanualtest.server.exception.base.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends BaseException {
  @Serial
  private static final long serialVersionUID = 1L;

  public AuthenticationException(String message) {
    super(message, ErrorCode.UNAUTHORIZED);
  }
  public AuthenticationException(Integer code) {
    super("Error", code);
  }
  public AuthenticationException(String exception, Integer code) {
    super(exception, code);
  }

}
