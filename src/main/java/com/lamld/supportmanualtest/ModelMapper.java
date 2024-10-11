package com.lamld.supportmanualtest;


import com.lamld.supportmanualtest.app.dto.account.AccountCreate;
import com.lamld.supportmanualtest.app.dto.project.ProjectDto;
import com.lamld.supportmanualtest.app.dto.testApi.TestApiDto;
import com.lamld.supportmanualtest.app.dto.testField.TestFieldDto;
import com.lamld.supportmanualtest.app.dto.validateConstrain.ValidateConstrainDto;
import com.lamld.supportmanualtest.app.response.account.AccountResponse;
import com.lamld.supportmanualtest.app.response.project.ProjectResponse;
import com.lamld.supportmanualtest.app.response.testApi.TestApiResponse;
import com.lamld.supportmanualtest.app.response.testField.TestFieldResponse;
import com.lamld.supportmanualtest.app.response.validateConstrain.ValidateConstrainResponse;
import com.lamld.supportmanualtest.server.entities.*;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ModelMapper {

  AccountResponse toAccountResponse(Account account);

  Account toAccount(AccountCreate accountCreate);

  Project toProject(ProjectDto projectDto);

  void mapToProject(@MappingTarget Project project, ProjectDto projectDto);

  ProjectResponse toProjectResponse(Project project);

  List<ProjectResponse> toProjectResponseList(List<Project> projects);

  default Page<AccountResponse> toPageAccountResponse(Page<Account> accounts){
    return accounts.map(this::toAccountResponse);
  }

  default Page<ProjectResponse> toPageProjectResponse(Page<Project> projects) {
    return projects.map(this::toProjectResponse);
  }

  ValidateConstrain toValidateConstrain(ValidateConstrainDto validateConstrainDto);

  ValidateConstrainResponse toValidateConstrainResponse(ValidateConstrain validateConstrain);

  void mapToValidateConstrain(@MappingTarget ValidateConstrain validateConstrain, ValidateConstrainDto validateConstrainDto);

  default Page<ValidateConstrainResponse> toPageValidateConstrainResponse(Page<ValidateConstrain> validateConstrainResponses){
    return validateConstrainResponses.map(this::toValidateConstrainResponse);
  }

  TestField toTestField(TestFieldDto testFieldDto);

  TestFieldResponse toTestFieldResponse(TestField testField);

  void mapToTestField(@MappingTarget TestField testField, TestFieldDto testFieldDto);

  default Page<TestFieldResponse> toPageTestFieldResponse(Page<TestField> testFieldResponses) {
    return null;
  }

  TestApi toTestApi(TestApiDto testApiDto);

  TestApiResponse toTestApiResponse(TestApi testApi);

  void mapToTestApi(@MappingTarget TestApi testApi, TestApiDto testApiDto);

  default Page<TestApiResponse> toPageTestApiResponse(Page<TestApi> testApis){
    return testApis.map(this::toTestApiResponse);
  }

  List<TestApiResponse> toTestApiResponseList(List<TestApi> testApis);
}
