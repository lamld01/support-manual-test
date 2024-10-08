package com.lamld.supportmanualtest.server.repositories;

import com.lamld.supportmanualtest.server.entities.TestField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TestFieldRepository extends JpaRepository<TestField, Integer>, JpaSpecificationExecutor<TestField> {
}