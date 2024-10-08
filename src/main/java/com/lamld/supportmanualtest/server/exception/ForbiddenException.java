package com.lamld.supportmanualtest.server.exception;

import com.lamld.supportmanualtest.server.exception.base.BaseException;
import com.lamld.supportmanualtest.server.exception.base.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ForbiddenException extends BaseException {
  private static final long serialVersionUID = 1L;

  public ForbiddenException(String exception) {
    super(exception, ErrorCode.FORBIDDEN);
  }
  public ForbiddenException(Integer code) {
    super("Error", code);
  }
  public ForbiddenException(String exception, Integer code) {
    super(exception, code);
  }
  public ForbiddenException() {
    super("Forbidden", 401);
  }

}
