package com.lamld.supportmanualtest.server.services;

import com.lamld.supportmanualtest.app.dto.project.ProjectDto;
import com.lamld.supportmanualtest.app.response.project.ProjectResponse;
import com.lamld.supportmanualtest.server.entities.Project;
import com.lamld.supportmanualtest.server.data.auth.AccountInfo;
import com.lamld.supportmanualtest.server.exception.BadRequestException;
import com.lamld.supportmanualtest.server.repositories.ProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService extends BaseService {

  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
  public ProjectResponse createProject(AccountInfo accountInfo, ProjectDto projectDto) {
    Project project = modelMapper.toProject(projectDto);
    project.setAccountId(accountInfo.getAccountId());
    if(projectDto.parentProjectId() == null) {
      project = projectStorage.save(project);
      project.setParentProjectId(project.getId());
      project.setRootProjectId(project.getId());
    }else{
      Project parentProject = projectStorage.findById(projectDto.parentProjectId()).orElseThrow(() -> new BadRequestException("Parent project not found"));
      project.setRootProjectId(parentProject.getRootProjectId());
      project.setParentProjectId(parentProject.getId());
    }
    projectStorage.save(project);
    return modelMapper.toProjectResponse(project);
  }

  public Project getProjectById(Integer accountId, Integer id) {
    return projectStorage.findByIdAndAccountId(id, accountId)
        .orElseThrow(() -> new BadRequestException("Project not found or access denied"));
  }

  public ProjectResponse findProjectById(AccountInfo accountInfo, Integer id) {
    Project project = getProjectById(accountInfo.getAccountId(), id);
    return modelMapper.toProjectResponse(project);
  }

  public List<ProjectResponse> getAllProjects(AccountInfo accountInfo, Integer parentId) {
    List<Project> projects = projectStorage.findAllByAccountIdAndParentProjectIdAndIdNot(accountInfo.getAccountId(), parentId, parentId);
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

  public Page<ProjectResponse> findProjectAccounts(AccountInfo accountInfo, String projectName, Pageable pageable) {
    Page<Project> projects = projectStorage.findByFilters(accountInfo.getAccountId(), projectName, pageable);
    return modelMapper.toPageProjectResponse(projects);
  }

  public List<ProjectResponse> findAll() {
    List<Project> projects = projectStorage.findAll();
    return modelMapper.toProjectResponseList(projects);
  }
}
