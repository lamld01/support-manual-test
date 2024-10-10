package com.lamld.supportmanualtest.server.storages;

import com.lamld.supportmanualtest.server.entities.Project;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ProjectStorage extends BaseStorage {

  public Project save(Project project) {
    return projectRepository.saveAndFlush(project);
  }

  public Optional<Project> findById(Integer id) {
    return projectRepository.findById(id);
  }

  public List<Project> findAll() {
    return projectRepository.findAll();
  }

  public void deleteById(Integer id) {
    projectRepository.deleteById(id);
  }

  public Optional<Project> findByIdAndAccountId(Integer id, Integer accountId) {
    return projectRepository.findByIdAndAccountId(id, accountId);
  }

  public List<Project> findAllByAccountId(Integer accountId) {
    return projectRepository.findAllByAccountId(accountId);
  }

  public void delete(Project project) {
    projectRepository.delete(project);
  }

  public Page<Project> findByFilters(Integer accountId, String projectName, Pageable pageable) {
    return projectRepository.findAll(filterByConditions(accountId, projectName), pageable);
  }

  private Specification<Project> filterByConditions(Integer accountId, String projectName) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (accountId!= null) {
        predicates.add(cb.equal(root.get("accountId"), accountId));
      }

      if (projectName!= null && !projectName.isEmpty()) {
        predicates.add(cb.like(cb.lower(root.get("projectName")), "%" + projectName.toLowerCase() + "%"));
      }
      predicates.add(cb.equal(root.get("id"), root.get("rootProjectId")));
      return cb.and(predicates.toArray(new Predicate[0]));
    };

  }

  public List<Project> findAllByAccountIdAndParentProjectIdAndIdNot(Integer accountId, Integer parentId, Integer id) {
    return projectRepository.findAllByAccountIdAndParentProjectIdAndIdNot(accountId, parentId, parentId);
  }
}
