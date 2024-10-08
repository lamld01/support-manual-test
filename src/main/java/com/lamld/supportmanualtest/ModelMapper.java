package com.lamld.supportmanualtest;


import com.lamld.supportmanualtest.app.dto.account.AccountCreate;
import com.lamld.supportmanualtest.app.dto.project.ProjectDto;
import com.lamld.supportmanualtest.app.response.account.AccountResponse;
import com.lamld.supportmanualtest.app.response.project.ProjectResponse;
import com.lamld.supportmanualtest.server.entities.Account;
import com.lamld.supportmanualtest.server.entities.Project;
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

  default Page<ProjectResponse> toPageProjectResponse(Page<Project> projects){
    return projects.map(this::toProjectResponse);
  }
}
