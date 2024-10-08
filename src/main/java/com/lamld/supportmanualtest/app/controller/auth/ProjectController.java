package com.lamld.supportmanualtest.app.controller.auth;

import com.lamld.supportmanualtest.app.controller.BaseController;
import com.lamld.supportmanualtest.app.dto.project.ProjectDto;
import com.lamld.supportmanualtest.app.response.BaseResponseDto;
import com.lamld.supportmanualtest.app.response.PageResponse;
import com.lamld.supportmanualtest.app.response.account.AccountResponse;
import com.lamld.supportmanualtest.app.response.project.ProjectResponse;
import com.lamld.supportmanualtest.server.services.ProjectService;
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

  @GetMapping("parent")
  public List<ProjectResponse> getAllProjects(Authentication authentication, @RequestParam Integer parentId) {
    return projectService.getAllProjects(getAccountInfo(authentication), parentId);
  }

  @GetMapping("page")
  @PageableAsQueryParam
  public BaseResponseDto<PageResponse<ProjectResponse>> getProjectPage(Authentication authentication,
                                                                       @RequestParam(required = false) String projectName,
                                                                       @Parameter(hidden = true) @PageableDefault(size = 20) Pageable pageable) {
    Page<ProjectResponse> pageResponse = projectService.findProjectAccounts(getAccountInfo(authentication), projectName, pageable);
    return new BaseResponseDto<>(PageResponse.createFrom(pageResponse));
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
