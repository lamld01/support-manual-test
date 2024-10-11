package com.lamld.supportmanualtest.app.controller.auth;

import com.lamld.supportmanualtest.app.controller.BaseController;
import com.lamld.supportmanualtest.app.dto.testApi.TestApiDto;
import com.lamld.supportmanualtest.app.response.BaseResponseDto;
import com.lamld.supportmanualtest.app.response.PageResponse;
import com.lamld.supportmanualtest.app.response.testApi.TestApiResponse;
import com.lamld.supportmanualtest.server.services.TestApiService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/manual-test/test-api")
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
public class TestApiController extends BaseController {

  private final TestApiService testApiService;

  @PostMapping
  public BaseResponseDto<TestApiResponse> createTestApi(Authentication authentication, @RequestBody TestApiDto testApiDto) {
    return new BaseResponseDto<>(testApiService.createTestApi(getAccountInfo(authentication), testApiDto));
  }

  @GetMapping("/{id}")
  public BaseResponseDto<TestApiResponse> getTestApiById(Authentication authentication, @PathVariable Integer id) {
    return new BaseResponseDto<>(testApiService.getTestApiById(getAccountInfo(authentication), id));
  }


  @GetMapping("page")
  @PageableAsQueryParam
  public BaseResponseDto<PageResponse<TestApiResponse>> getTestApiPage(Authentication authentication,
                                                                       @RequestParam(required = false) Integer projectId,
                                                                       @RequestParam(required = false) String apiName,
                                                                       @RequestParam(required = false) String description,
                                                                       @Parameter(hidden = true) @PageableDefault(size = 20) Pageable pageable) {
    Page<TestApiResponse> pageResponse = testApiService.findTestApi(getAccountInfo(authentication), projectId, apiName, description, pageable);
    return new BaseResponseDto<>(PageResponse.createFrom(pageResponse));
  }

  @PutMapping("/{id}")
  public TestApiResponse updateTestApi(Authentication authentication, @PathVariable Integer id, @RequestBody TestApiDto testApiDto) {
    return testApiService.updateTestApi(getAccountInfo(authentication), id, testApiDto);
  }

  @DeleteMapping("/{id}")
  public void deleteTestApi(Authentication authentication, @PathVariable Integer id) {
    testApiService.deleteTestApi(getAccountInfo(authentication), id);
  }
}
