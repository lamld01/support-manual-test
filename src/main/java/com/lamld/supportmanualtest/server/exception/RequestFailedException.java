package com.lamld.supportmanualtest.server.exception;

import com.lamld.supportmanualtest.server.exception.base.BaseException;
import com.lamld.supportmanualtest.server.exception.base.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequestFailedException extends BaseException {

  public RequestFailedException(String message) {
    super(message, ErrorCode.BAD_REQUEST);
  }

  public RequestFailedException() {
    super("Request failed", ErrorCode.BAD_REQUEST);
  }
}
