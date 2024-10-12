package com.lamld.supportmanualtest.server.repositories;

import com.lamld.supportmanualtest.server.entities.TestField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public interface TestFieldRepository extends JpaRepository<TestField, Integer>, JpaSpecificationExecutor<TestField> {
  Optional<TestField> findByFieldName(String name);

  List<TestField> findByIdIn(HashSet<Integer> values);
}