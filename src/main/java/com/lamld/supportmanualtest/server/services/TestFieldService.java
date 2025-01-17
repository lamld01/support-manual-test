package com.lamld.supportmanualtest.server.services;

import com.lamld.supportmanualtest.app.dto.testField.TestFieldDto;
import com.lamld.supportmanualtest.app.response.project.ProjectResponse;
import com.lamld.supportmanualtest.app.response.testField.TestFieldResponse;
import com.lamld.supportmanualtest.app.response.validateConstrain.ValidateConstrainResponse;
import com.lamld.supportmanualtest.server.data.auth.AccountInfo;
import com.lamld.supportmanualtest.server.entities.TestApi;
import com.lamld.supportmanualtest.server.entities.TestField;
import com.lamld.supportmanualtest.server.entities.ValidateConstrain;
import com.lamld.supportmanualtest.server.exception.BadRequestException;
import com.lamld.supportmanualtest.server.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestFieldService extends BaseService {

  private final ValidateConstrainService validateConstrainService;
  private final ProjectService projectService;
  private final TestApiService testApiService;

  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
  public TestFieldResponse createTestField(AccountInfo accountInfo, TestFieldDto testFieldDto) {
    boolean isExist = testFieldStorage.existsByFieldCodeAndProjectId(testFieldDto.fieldCode(), testFieldDto.projectId());
    if (isExist) {
      throw new BadRequestException("Field code already exists in the project");
    }
    TestField testField = testFieldStorage.findByFieldName(testFieldDto.fieldName())
        .orElse(modelMapper.toTestField(testFieldDto));
    testField.setAccountId(accountInfo.getAccountId());
    testField = testFieldStorage.save(testField);
    return modelMapper.toTestFieldResponse(testField);
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
  public TestFieldResponse updateTestField(AccountInfo accountInfo, Integer id, TestFieldDto testFieldDto) {
    TestField testField = testFieldStorage.findById(id)
        .orElseThrow(() -> new BadRequestException("Validate constrain not found"));
    modelMapper.mapToTestField(testField, testFieldDto);
    testFieldStorage.save(testField);
    return modelMapper.toTestFieldResponse(testField);
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
  public void deleteTestField(AccountInfo accountInfo, Integer id) {
    TestField testField = testFieldStorage.findById(id)
        .orElseThrow(() -> new BadRequestException("Validate constrain not found"));
    testFieldStorage.delete(testField);
  }

  @Transactional(readOnly = true)
  public TestFieldResponse findTestFieldById(AccountInfo accountInfo, Integer id) {
    return testFieldStorage.findById(id)
        .map(testField -> modelMapper.toTestFieldResponse(testField))
        .orElseThrow(() -> new BadRequestException("Validate constrain not found"));
  }

  @Transactional(readOnly = true)
  public Page<TestFieldResponse> findTestField(AccountInfo accountInfo, Integer apiId, Integer projectId, String fieldName, String fieldCode, List<Integer> constrainIds, Pageable pageable) {
    List<Integer> fieldIds = null;

    if (apiId != null) {
      TestApi testApi = testApiService.findTestApiById(accountInfo.getAccountId(), apiId);
      fieldIds = JsonUtil.getValuesByKey(new JSONObject(testApi.getBody()), "value", Integer.class)
          .stream()
          .toList(); // Hoặc bạn có thể xử lý các giá trị này theo cách bạn muốn
    }
    Page<TestField> testFieldPage = testFieldStorage.findPage(accountInfo.getAccountId(), fieldIds, projectId, fieldName, fieldCode, constrainIds, pageable);

    List<ValidateConstrain> validateConstrains = validateConstrainService.findAll();
    List<ProjectResponse> projects = projectService.findAll();

    // Map TestField to TestFieldResponse including ValidateConstrainResponse
    List<TestFieldResponse> testFieldResponses = testFieldPage.stream()
        .map(testField -> createTestFieldResponse(testField, projects, validateConstrains))
        .toList();

    return new PageImpl<>(testFieldResponses, pageable, testFieldPage.getTotalElements());
  }

  // Hàm tạo TestFieldResponse riêng
  private TestFieldResponse createTestFieldResponse(TestField testField, List<ProjectResponse> projects, List<ValidateConstrain> validateConstrains) {
    // Map to ValidateConstrainResponse
    List<ValidateConstrainResponse> validateConstrainResponses = validateConstrains.stream()
        .filter(constrain -> testField.getValidateConstrainIds().contains(constrain.getId()))
        .map(validateConstrain -> modelMapper.toValidateConstrainResponse(validateConstrain)) // Assuming you have a method to convert ValidateConstrain to ValidateConstrainResponse
        .toList();

    return new TestFieldResponse(
        testField.getId(),
        testField.getFieldName(),
        projects.stream().filter(project -> project.id().equals(testField.getProjectId())).findFirst().orElse(null),
        testField.getDescription(),
        testField.getFieldCode(),
        testField.getDefaultRegexValue(),
        validateConstrainResponses
    );
  }

  public List<TestField> findByIdIn(HashSet<Integer> values) {
    return testFieldStorage.findByIdIn(values);
  }
}
