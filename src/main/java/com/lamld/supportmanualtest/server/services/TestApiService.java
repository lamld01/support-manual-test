package com.lamld.supportmanualtest.server.services;

import com.lamld.supportmanualtest.app.dto.testApi.TestApiDto;
import com.lamld.supportmanualtest.app.response.project.ProjectResponse;
import com.lamld.supportmanualtest.app.response.testApi.TestApiResponse;
import com.lamld.supportmanualtest.server.data.auth.AccountInfo;
import com.lamld.supportmanualtest.server.data.pojo.JsonInfo;
import com.lamld.supportmanualtest.server.data.pojo.KeyValue;
import com.lamld.supportmanualtest.server.data.pojo.RequestResponse;
import com.lamld.supportmanualtest.server.data.type.ObjectType;
import com.lamld.supportmanualtest.server.entities.Project;
import com.lamld.supportmanualtest.server.entities.TestApi;
import com.lamld.supportmanualtest.server.entities.TestField;
import com.lamld.supportmanualtest.server.exception.BadRequestException;
import com.lamld.supportmanualtest.server.utils.RegexUtil;
import com.lamld.supportmanualtest.server.utils.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestApiService extends BaseService {

  private final ProjectService projectService;
  private final TestFieldService testFieldService;

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

  public Object generateApiBody(AccountInfo accountInfo, Integer id) {
    TestApi testApi = findTestApiById(accountInfo.getAccountId(), id);
    return generateApiBody(accountInfo, testApi);
  }

  public Object generateApiBody(AccountInfo accountInfo, TestApi testApi) {
    JSONObject response = new JSONObject(); // Initialize JSONObject
    HashSet<Integer> valueIds = new HashSet<>();
    JsonInfo body = testApi.getBody(); // Declare body outside the if statement

    if (body != null && !body.getChildren().isEmpty()) {
      // Handle OBJECT type
      if (body.getType() == ObjectType.OBJECT) {
        for (JsonInfo child : body.getChildren()) {
          response.put(child.getName(), child.getValue());
          if (child.getValue() != null) { // Add value to the list if not null
            valueIds.add(child.getValue());
          }
        }
      }
      // Handle ARRAY_STRING or ARRAY_OBJECT types
      else if (body.getType() == ObjectType.ARRAY_STRING || body.getType() == ObjectType.ARRAY_OBJECT) {
        JSONArray jsonArray = new JSONArray(); // Initialize JSONArray
        for (JsonInfo child : body.getChildren()) {
          JSONObject childJson = new JSONObject();
          childJson.put("name", child.getName());
          childJson.put("value", child.getValue());
          jsonArray.put(childJson); // Add each child as a JSONObject

          // Collecting values
          if (child.getValue() != null) { // Add value to the list if not null
            valueIds.add(child.getValue());
          }
        }
        response.put(body.getName(), jsonArray); // Add the array to the response
      }
    }

    // Fetch TestField names based on the collected value IDs
    List<TestField> testFields = testFieldService.findByIdIn(valueIds);

    // Create a map for quick look-up of TestField names by their IDs
    Map<Integer, String> idToNameMap = testFields.stream()
        .collect(Collectors.toMap(TestField::getId, t -> RegexUtil.generateRegexValue(t.getDefaultRegexValue())));

    // Replace value IDs in the response with corresponding names
    if (body != null && body.getType() == ObjectType.OBJECT) {
      for (JsonInfo child : body.getChildren()) {
        if (child.getValue() != null && idToNameMap.containsKey(child.getValue())) {
          response.put(child.getName(), idToNameMap.get(child.getValue()));
        }
      }
    } else if (body != null && (body.getType() == ObjectType.ARRAY_STRING || body.getType() == ObjectType.ARRAY_OBJECT)) {
      JSONArray jsonArray = response.getJSONArray(body.getName());
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject childJson = jsonArray.getJSONObject(i);
        Integer valueId = (Integer) childJson.get("value"); // Ensure the type matches
        if (idToNameMap.containsKey(valueId)) {
          childJson.put("value", idToNameMap.get(valueId)); // Replace ID with name
        }
      }
    }
    return response.toMap(); // Return the constructed JSON object
  }

  public RequestResponse requestApi(AccountInfo accountInfo, Integer id) {
    TestApi testApi = findTestApiById(accountInfo.getAccountId(), id);
    Project project = projectService.getProjectById(accountInfo.getAccountId(), testApi.getProjectId());
    Object body = generateApiBody(accountInfo, testApi);
    return RequestUtil.sendRequest(project.getApiBaseUrl() + testApi.getPath(), testApi.getMethod(), body, testApi.getHeader(), testApi.getParam());
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
        testApi.getPath(),
        testApi.getParam(),
        testApi.getBody(),
        testApi.getHeader()
    );
  }

}
