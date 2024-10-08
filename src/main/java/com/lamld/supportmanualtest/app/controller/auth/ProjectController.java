package com.lamld.supportmanualtest.app.controller.auth;

import com.lamld.supportmanualtest.app.controller.BaseController;
import com.lamld.supportmanualtest.app.dto.project.ProjectDto;
import com.lamld.supportmanualtest.app.response.project.ProjectResponse;
import com.lamld.supportmanualtest.server.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/manual-test/projects")
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
public class ProjectController extends BaseController {
  private final ProjectService projectService;

  @PostMapping
  public ProjectResponse createProject(Authentication authentication,  @RequestBody ProjectDto projectDto) {
    return projectService.createProject(getAccountInfo(authentication), projectDto);
  }

  @GetMapping("/{id}")
  public ProjectResponse getProjectById(Authentication authentication,@PathVariable Integer id) {
    return projectService.findProjectById(getAccountInfo(authentication),id);
  }

  @GetMapping
  public List<ProjectResponse> getAllProjects(Authentication authentication) {
    return projectService.getAllProjects(getAccountInfo(authentication));
  }

  @PutMapping("/{id}")
  public ProjectResponse updateProject(Authentication authentication, @PathVariable Integer id, @RequestBody ProjectDto projectDto) {
    return projectService.updateProject(getAccountInfo(authentication),id, projectDto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteProject(Authentication authentication, @PathVariable Integer id) {
    projectService.deleteProject(getAccountInfo(authentication),id);
  }
}
