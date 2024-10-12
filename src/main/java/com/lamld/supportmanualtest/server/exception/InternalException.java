package com.lamld.supportmanualtest.server.exception;

import com.lamld.supportmanualtest.server.exception.base.BaseException;
import com.lamld.supportmanualtest.server.exception.base.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalException extends BaseException {

  public InternalException(String message) {
    super(message, ErrorCode.INTERNAL_SERVER_ERROR);
  }

  public InternalException() {
    super("Internal server error", ErrorCode.INTERNAL_SERVER_ERROR);
  }
}
