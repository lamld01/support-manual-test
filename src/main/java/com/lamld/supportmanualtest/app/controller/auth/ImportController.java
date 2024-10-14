package com.lamld.supportmanualtest.app.controller.auth;

import com.lamld.supportmanualtest.app.controller.BaseController;
import com.lamld.supportmanualtest.app.response.BaseResponseDto;
import com.lamld.supportmanualtest.app.response.importResponse.ImportResponse;
import com.lamld.supportmanualtest.server.data.pojo.OpenApiDefinition.OpenApiDefinition;
import com.lamld.supportmanualtest.server.services.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/manual-test/import")
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
public class ImportController extends BaseController {
  private final ImportService importService;

  @PostMapping("open-api-doc")
  public BaseResponseDto<ImportResponse> importOpenApiDoc(Authentication authentication, @RequestBody OpenApiDefinition document) {
    return new  BaseResponseDto<>(importService.importOpenApiDoc(getAccountInfo(authentication), document));
  }
}
