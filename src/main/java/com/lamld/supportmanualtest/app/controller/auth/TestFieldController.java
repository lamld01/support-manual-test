package com.lamld.supportmanualtest.app.controller.auth;

import com.lamld.supportmanualtest.app.controller.BaseController;
import com.lamld.supportmanualtest.app.dto.testField.TestFieldDto;
import com.lamld.supportmanualtest.app.response.BaseResponseDto;
import com.lamld.supportmanualtest.app.response.PageResponse;
import com.lamld.supportmanualtest.app.response.testField.TestFieldResponse;
import com.lamld.supportmanualtest.server.data.type.StatusEnum;
import com.lamld.supportmanualtest.server.services.TestFieldService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("v1/manual-test/test-field")
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
public class TestFieldController extends BaseController {

  private final TestFieldService testFieldService;

  @PostMapping
  public TestFieldResponse createTestField(Authentication authentication, @RequestBody TestFieldDto testFieldDto) {
    return testFieldService.createTestField(getAccountInfo(authentication), testFieldDto);
  }

  @GetMapping("/{id}")
  public TestFieldResponse getTestFieldById(Authentication authentication, @PathVariable Integer id) {
    return testFieldService.findTestFieldById(getAccountInfo(authentication), id);
  }


  @GetMapping("page")
  @PageableAsQueryParam
  public BaseResponseDto<PageResponse<TestFieldResponse>> getTestFieldPage(Authentication authentication,
                                                                           @RequestParam(required = false) Integer apiId,
                                                                           @RequestParam(required = false) Integer projectId,
                                                                           @RequestParam(required = false) String fieldName,
                                                                           @RequestParam(required = false) String fieldCode,
                                                                           @RequestParam(required = false) List<Integer> constrainIds,
                                                                           @Parameter(hidden = true) @PageableDefault(size = 20) Pageable pageable) {
    Page<TestFieldResponse> pageResponse = testFieldService.findTestField(getAccountInfo(authentication),apiId, projectId, fieldName, fieldCode, constrainIds, pageable);
    return new BaseResponseDto<>(PageResponse.createFrom(pageResponse));
  }

  @PutMapping("/{id}")
  public TestFieldResponse updateTestField(Authentication authentication, @PathVariable Integer id, @RequestBody TestFieldDto testFieldDto) {
    return testFieldService.updateTestField(getAccountInfo(authentication), id, testFieldDto);
  }

  @DeleteMapping("/{id}")
  public void deleteTestField(Authentication authentication, @PathVariable Integer id) {
    testFieldService.deleteTestField(getAccountInfo(authentication), id);
  }

}
