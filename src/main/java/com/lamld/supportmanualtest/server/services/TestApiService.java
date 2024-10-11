package com.lamld.supportmanualtest.server.services;

import com.lamld.supportmanualtest.app.dto.testApi.TestApiDto;
import com.lamld.supportmanualtest.app.response.project.ProjectResponse;
import com.lamld.supportmanualtest.app.response.testApi.TestApiResponse;
import com.lamld.supportmanualtest.server.data.auth.AccountInfo;
import com.lamld.supportmanualtest.server.entities.TestApi;
import com.lamld.supportmanualtest.server.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestApiService extends BaseService {

  private final ProjectService projectService;

  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
  public TestApiResponse createTestApi(AccountInfo accountInfo, TestApiDto testApiDto) {
    TestApi testApi = modelMapper.toTestApi(testApiDto);
    testApi.setAccountId(accountInfo.getAccountId());
    testApi = testApiStorage.save(testApi);
    return modelMapper.toTestApiResponse(testApi);
  }

  public TestApiResponse getTestApiById(AccountInfo accountInfo, Integer id) {
    TestApi testApi = testApiStorage.findByAccountIdAndId(accountInfo.getAccountId(), id)
        .orElseThrow(() -> new RuntimeException("TestApi not found"));
    ProjectResponse projectResponse = projectService.findProjectById(accountInfo, testApi.getProjectId());

    return createTestApiResponse(testApi, projectResponse);
  }

  public TestApi findTestApiById(Integer accountId, Integer id) {
    return testApiStorage.findByAccountIdAndId(accountId, id)
        .orElseThrow(() -> new RuntimeException("TestApi not found"));
  }

  public TestApiResponse updateTestApi(AccountInfo accountInfo, Integer id, TestApiDto testApiDto) {
    TestApi testApi = testApiStorage.findByAccountIdAndId(accountInfo.getAccountId(), id)
        .orElseThrow(() -> new BadRequestException("TestApi not found"));
    modelMapper.mapToTestApi(testApi, testApiDto);
    return modelMapper.toTestApiResponse(testApiStorage.save(testApi));
  }

  public void deleteTestApi(AccountInfo accountInfo, Integer id) {
    TestApi testApi = findTestApiById(accountInfo.getAccountId(), id);
    testApiStorage.delete(testApi);
  }

  public Page<TestApiResponse> findTestApi(AccountInfo accountInfo, Integer projectId, String apiName, String description, Pageable pageable) {
    Page<TestApi> testApis = testApiStorage.findByFilters(accountInfo.getAccountId(), projectId, apiName, description, pageable);
    List<ProjectResponse> projects = projectService.findAll();
    List<TestApiResponse> testApiResponses = testApis.stream()
        .map(testApi -> createTestApiResponse(testApi,
            projects.stream().filter(project -> project.id().equals(testApi.getProjectId())).findFirst().orElse(null)))
        .toList();

    return new PageImpl<>(testApiResponses, pageable, testApis.getTotalElements());
  }

  public List<TestApiResponse> findAll() {
    List<TestApi> testApis = testApiStorage.findAll();
    return modelMapper.toTestApiResponseList(testApis);
  }

  // Hàm tạo TestApiResponse riêng
  private TestApiResponse createTestApiResponse(TestApi testApi, ProjectResponse projectResponse) {
    return new TestApiResponse(
        testApi.getId(),
        projectResponse,
        testApi.getApiName(),
        testApi.getDescription(),
        testApi.getMethod(),
        testApi.getParam(),
        testApi.getBody(),
        testApi.getHeader()
    );
  }
}
