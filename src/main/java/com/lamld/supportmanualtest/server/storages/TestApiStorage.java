package com.lamld.supportmanualtest.server.storages;

import com.lamld.supportmanualtest.server.entities.TestApi;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TestApiStorage extends BaseStorage {

  public TestApi save(TestApi testApi) {
    return testApiRepository.save(testApi);
  }

  public Optional<TestApi> findByAccountIdAndId(Integer accountId, Integer id) {
    return testApiRepository.findByAccountIdAndId(accountId, id);
  }

  public void delete(TestApi testApi) {
    testApiRepository.delete(testApi);
  }

  public Page<TestApi> findByFilters(Integer accountId, Integer projectId, String apiName, String description, Pageable pageable) {
    return testApiRepository.findAll(filterByConditions(accountId, projectId, apiName, description), pageable);
  }

  private Specification<TestApi> filterByConditions(Integer accountId, Integer projectId, String apiName, String description) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (accountId != null) {
        predicates.add(criteriaBuilder.equal(root.get("accountId"), accountId));
      }
      if (projectId != null) {
        predicates.add(criteriaBuilder.equal(root.get("projectId"), projectId));
      }
      if (apiName != null && !apiName.isEmpty()) {
        predicates.add(criteriaBuilder.equal(root.get("apiName"), apiName));
      }
      if (description != null && !description.isEmpty()) {
        predicates.add(criteriaBuilder.equal(root.get("description"), description));
      }
      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }

  public List<TestApi> findAll() {
    return testApiRepository.findAll();
  }
}
