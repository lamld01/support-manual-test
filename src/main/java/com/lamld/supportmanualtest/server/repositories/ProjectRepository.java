package com.lamld.supportmanualtest.server.repositories;

import com.lamld.supportmanualtest.server.entities.Account;
import com.lamld.supportmanualtest.server.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Integer>, JpaSpecificationExecutor<Project> {
  Optional<Project> findByIdAndAccountId(Integer id, Integer accountId);

  List<Project> findAllByAccountId(Integer accountId);
}