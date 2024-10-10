package com.lamld.supportmanualtest.app.controller.auth;

import com.lamld.supportmanualtest.app.controller.BaseController;
import com.lamld.supportmanualtest.app.dto.validateConstrain.ValidateConstrainDto;
import com.lamld.supportmanualtest.app.response.BaseResponseDto;
import com.lamld.supportmanualtest.app.response.PageResponse;
import com.lamld.supportmanualtest.app.response.validateConstrain.ValidateConstrainResponse;
import com.lamld.supportmanualtest.server.data.type.StatusEnum;
import com.lamld.supportmanualtest.server.services.ValidateConstrainService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/manual-test/validate-constrains")
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
public class ValidateConstrainController extends BaseController {

  private final ValidateConstrainService validateConstrainService;

  @PostMapping
  public ValidateConstrainResponse createValidateConstrain(Authentication authentication, @RequestBody ValidateConstrainDto validateConstrainDto) {
    return validateConstrainService.createValidateConstrain(getAccountInfo(authentication), validateConstrainDto);
  }

  @GetMapping("/{id}")
  public ValidateConstrainResponse getValidateConstrainById(Authentication authentication, @PathVariable Integer id) {
    return validateConstrainService.findValidateConstrainById(getAccountInfo(authentication), id);
  }


  @GetMapping("page")
  @PageableAsQueryParam
  public BaseResponseDto<PageResponse<ValidateConstrainResponse>> getValidateConstrainPage(Authentication authentication,
                                                                                           @RequestParam(required = false) String validateConstrainName,
                                                                                           @RequestParam(required = false) String regexValue,
                                                                                           @RequestParam(required = false) StatusEnum status,
                                                                                           @Parameter(hidden = true) @PageableDefault(size = 20) Pageable pageable) {
    Page<ValidateConstrainResponse> pageResponse = validateConstrainService.findValidateConstrain(getAccountInfo(authentication), validateConstrainName, regexValue, status, pageable);
    return new BaseResponseDto<>(PageResponse.createFrom(pageResponse));
  }

  @PutMapping("/{id}")
  public ValidateConstrainResponse updateValidateConstrain(Authentication authentication, @PathVariable Integer id, @RequestBody ValidateConstrainDto validateConstrainDto) {
    return validateConstrainService.updateValidateConstrain(getAccountInfo(authentication), id, validateConstrainDto);
  }

  @DeleteMapping("/{id}")
  public void deleteValidateConstrain(Authentication authentication, @PathVariable Integer id) {
    validateConstrainService.deleteValidateConstrain(getAccountInfo(authentication), id);
  }
}
