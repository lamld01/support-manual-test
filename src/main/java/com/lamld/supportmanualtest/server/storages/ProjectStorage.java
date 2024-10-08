package com.lamld.supportmanualtest.server.storages;

import com.lamld.supportmanualtest.server.entities.Account;
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
    return projectRepository.save(project);
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
}
