package com.lamld.supportmanualtest.server.services;

import com.lamld.supportmanualtest.app.dto.testApi.TestApiDto;
import com.lamld.supportmanualtest.app.response.project.ProjectResponse;
import com.lamld.supportmanualtest.app.response.testApi.TestApiResponse;
import com.lamld.supportmanualtest.app.response.testApi.ValidateTestApiResponse;
import com.lamld.supportmanualtest.server.data.auth.AccountInfo;
import com.lamld.supportmanualtest.server.data.pojo.JsonInfo;
import com.lamld.supportmanualtest.server.data.pojo.KeyValue;
import com.lamld.supportmanualtest.server.data.pojo.RequestResponse;
import com.lamld.supportmanualtest.server.data.type.ObjectType;
import com.lamld.supportmanualtest.server.entities.Project;
import com.lamld.supportmanualtest.server.entities.TestApi;
import com.lamld.supportmanualtest.server.entities.TestField;
import com.lamld.supportmanualtest.server.entities.ValidateConstrain;
import com.lamld.supportmanualtest.server.exception.BadRequestException;
import com.lamld.supportmanualtest.server.utils.RegexUtil;
import com.lamld.supportmanualtest.server.utils.RequestUtil;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class TestApiService extends BaseService {

  private final ProjectService projectService;
  private final TestFieldService testFieldService;
  private final ValidateConstrainService validateConstrainService;

  @Lazy
  public TestApiService(ProjectService projectService, TestFieldService testFieldService, ValidateConstrainService validateConstrainService) {
    this.projectService = projectService;
    this.testFieldService = testFieldService;
    this.validateConstrainService = validateConstrainService;
  }

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
        .orElseThrow(() -> new BadRequestException("TestApi not found"));
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
    RequestResponse response = new RequestResponse();
    TestApi testApi = findTestApiById(accountInfo.getAccountId(), id);
    Project project = projectService.getProjectById(accountInfo.getAccountId(), testApi.getProjectId());
    testApi.getHeader().add(KeyValue.createAuthorityToken(project.getToken()));
    Object body = generateApiBody(accountInfo, testApi);
    response.setRequest(body);
    return RequestUtil.sendRequest(project.getApiBaseUrl() + testApi.getPath(), testApi.getMethod(), body, testApi.getHeader(), testApi.getParam(), 0);
  }

  public List<TestApiResponse> findAll() {
    List<TestApi> testApis = testApiStorage.findAll();
    return modelMapper.toTestApiResponseList(testApis);
  }

  // Hàm tạo TestApiResponse riêng
  private TestApiResponse createTestApiResponse(TestApi testApi, ProjectResponse projectResponse) {
    TestApiResponse response = modelMapper.toTestApiResponse(testApi);
    response.setProject(projectResponse);
    return response;
  }

  public List<ValidateTestApiResponse> generateInvalidateApiBody(AccountInfo accountInfo, Integer apiId) {
    TestApi testApi = findTestApiById(accountInfo.getAccountId(), apiId);
    return generateInvalidateApiBody(accountInfo, testApi);
  }
  public List<ValidateTestApiResponse> generateInvalidateApiBody(AccountInfo accountInfo, TestApi testApi) {
    List<ValidateTestApiResponse> testCases = new ArrayList<>(); // Danh sách test case
    log.info("Generating invalid API body for TestApi ID: {}", testApi.getId());

    JSONObject tempBody = new JSONObject(); // Initialize JSONObject
    HashSet<Integer> valueIds = new HashSet<>();
    JsonInfo body = testApi.getBody(); // Declare body outside the if statement

    if (body != null && !body.getChildren().isEmpty()) {
      log.debug("Processing body of type: {}", body.getType());

      // Handle OBJECT type
      if (body.getType() == ObjectType.OBJECT) {
        for (JsonInfo child : body.getChildren()) {
          tempBody.put(child.getName(), String.format("<<%s>>", child.getName()));
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
          childJson.put("value", String.format("<<%s>>", child.getName())); // Temporary placeholder
          jsonArray.put(childJson); // Add each child as a JSONObject

          // Collecting values
          if (child.getValue() != null) { // Add value to the list if not null
            valueIds.add(child.getValue());
          }
        }
        tempBody.put(body.getName(), jsonArray); // Add the array to the response
      }
    }

    log.debug("Value IDs collected: {}", valueIds);

    // Lấy TestField và ValidateConstrain liên quan
    List<TestField> testFields = testFieldService.findByIdIn(valueIds);
    log.info("Found {} TestFields related to the value IDs.", testFields.size());

    List<Integer> allValidateConstrainIds = testFields.stream()
        .flatMap(testField -> testField.getValidateConstrainIds().stream())
        .distinct()
        .toList();
    List<ValidateConstrain> validateConstrains = validateConstrainService.findByInIn(allValidateConstrainIds);

    log.info("Found {} ValidateConstrains related to the TestFields.", validateConstrains.size());

    // Tạo map FieldId -> ValidateConstrain
    Map<Integer, List<ValidateConstrain>> fieldIdToConstrainsMap = testFields.stream()
        .collect(Collectors.toMap(
            TestField::getId,
            testField -> validateConstrains.stream()
                .filter(vc -> testField.getValidateConstrainIds().contains(vc.getId()))
                .collect(Collectors.toList())
        ));

    // Tạo các trường hợp test với dữ liệu đúng từ TestField và dữ liệu sai từ ValidateConstrain
    for (TestField testField : testFields) {
      List<ValidateConstrain> constraints = fieldIdToConstrainsMap.get(testField.getId());

      log.debug("Generating test cases for field: {}", testField.getFieldCode());

      for (ValidateConstrain invalidConstraint : constraints) {
        // Clone tempBody for each test case to avoid modifying the original body
        JSONObject currentBody = new JSONObject(tempBody.toString());
        ValidateTestApiResponse testCase = new ValidateTestApiResponse();

        // Replace valid values for all fields except the current field being invalidated
        for (TestField otherField : testFields) {
          if (!otherField.equals(testField)) {
            // Replace other fields with valid values
            currentBody.put(otherField.getFieldCode(), RegexUtil.generateRegexValue(otherField.getDefaultRegexValue()));
          }
        }

        // Replace invalid value for the current field being tested
        currentBody.put(testField.getFieldCode(), RegexUtil.generateRegexValue(invalidConstraint.getRegexValue()));

        testCase.setBody(currentBody.toMap()); // Đưa body vào test case
        testCase.setTestCase(testField.getFieldCode() + " " + invalidConstraint.getConstrainName()); // Ghi lại điều kiện nào sai
        testCases.add(testCase); // Thêm test case vào danh sách

        log.debug("Generated test case: {}", testCase.getTestCase());
      }
    }

    log.info("Generated {} invalid test cases.", testCases.size());
    return testCases; // Trả về danh sách các trường hợp test
  }


  public List<RequestResponse> requestInvalidApi(AccountInfo accountInfo, Integer id) {
    List<RequestResponse> requestResponses = new ArrayList<>();
    log.info("Requesting invalid API for TestApi ID: {}", id);

    TestApi testApi = findTestApiById(accountInfo.getAccountId(), id);
    Project project = projectService.getProjectById(accountInfo.getAccountId(), testApi.getProjectId());

    List<ValidateTestApiResponse> body = generateInvalidateApiBody(accountInfo, testApi);
    testApi.getHeader().add(KeyValue.createAuthorityToken(project.getToken()));

    for (ValidateTestApiResponse testCase : body) {
      log.debug("Sending request for test case: {}", testCase.getTestCase());
      RequestResponse response = RequestUtil.sendRequest(project.getApiBaseUrl() + testApi.getPath(), testApi.getMethod(), testCase.getBody(), testApi.getHeader(), testApi.getParam(), 0);
      response.setTestCase(testCase.getTestCase());
      requestResponses.add(response);

      log.info("Received response for test case: {}", testCase.getTestCase());
    }

    log.info("Completed requests for {} test cases.", requestResponses.size());
    return requestResponses; // Trả về danh sách kết quả request
  }


}
