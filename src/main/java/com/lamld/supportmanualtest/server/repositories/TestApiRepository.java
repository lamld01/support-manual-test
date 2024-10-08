package com.lamld.supportmanualtest.server.repositories;

import com.lamld.supportmanualtest.server.entities.TestApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TestApiRepository extends JpaRepository<TestApi, Integer>, JpaSpecificationExecutor<TestApi> {
}