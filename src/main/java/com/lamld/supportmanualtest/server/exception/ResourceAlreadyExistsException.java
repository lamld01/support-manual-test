
package com.lamld.supportmanualtest.server.exception;
import com.lamld.supportmanualtest.server.exception.base.BaseException;
import com.lamld.supportmanualtest.server.exception.base.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceAlreadyExistsException extends BaseException {
  private static Map<String, String> lang = new HashMap<>();
  static {
    lang.put("vi", "Không tồn tại dữ liệu");
    lang.put("en", "Resource not found");
  }

  private static final long serialVersionUID = 1L;

  public ResourceAlreadyExistsException(String exception) {
    super(exception, ErrorCode.BAD_REQUEST);
  }

  public ResourceAlreadyExistsException() {
    super(lang.get("vi"), ErrorCode.BAD_REQUEST);
  }
}
