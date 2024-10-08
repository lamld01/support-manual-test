package com.lamld.supportmanualtest.server.services;

import com.lamld.supportmanualtest.app.dto.project.ProjectDto;
import com.lamld.supportmanualtest.app.response.project.ProjectResponse;
import com.lamld.supportmanualtest.server.entities.Project;
import com.lamld.supportmanualtest.server.data.auth.AccountInfo;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService extends BaseService {


  public ProjectResponse createProject(AccountInfo accountInfo, ProjectDto projectDto) {
    Project project = modelMapper.toProject(projectDto);
    project.setAccountId(accountInfo.getAccountId());
    projectStorage.save(project);
    return modelMapper.toProjectResponse(project);
  }

  public Project getProjectById(Integer accountId, Integer id) {
    return projectStorage.findByIdAndAccountId(id, accountId)
        .orElseThrow(() -> new RuntimeException("Project not found or access denied"));
  }

  public ProjectResponse findProjectById(AccountInfo accountInfo, Integer id) {
    Project project = getProjectById(accountInfo.getAccountId(), id);
    return modelMapper.toProjectResponse(project);
  }

  public List<ProjectResponse> getAllProjects(AccountInfo accountInfo) {
    List<Project> projects = projectStorage.findAllByAccountId(accountInfo.getAccountId());
    return modelMapper.toProjectResponseList(projects);
  }

  public ProjectResponse updateProject(AccountInfo accountInfo, Integer id, ProjectDto projectDto) {
    Project project = getProjectById(accountInfo.getAccountId(), id);
    modelMapper.mapToProject(project, projectDto);
    projectStorage.save(project);
    return modelMapper.toProjectResponse(project);
  }

  public void deleteProject(AccountInfo accountInfo, Integer id) {
    Project project = getProjectById(accountInfo.getAccountId(), id);
    projectStorage.delete(project);
  }
}
