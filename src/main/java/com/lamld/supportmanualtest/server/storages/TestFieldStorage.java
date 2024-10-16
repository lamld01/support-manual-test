package com.lamld.supportmanualtest.server.storages;

import com.lamld.supportmanualtest.server.data.type.StatusEnum;
import com.lamld.supportmanualtest.server.entities.TestField;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
public class TestFieldStorage extends BaseStorage {

  public Optional<TestField> findByFieldName(String name) {
    return testFieldRepository.findByFieldName(name);
  }

  public TestField save(TestField testField) {
    return testFieldRepository.saveAndFlush(testField);
  }

  public Optional<TestField> findById(Integer id) {
    return testFieldRepository.findById(id);
  }

  public void delete(TestField testField) {
    testFieldRepository.delete(testField);
  }

  public Page<TestField> findPage(Integer accountId, List<Integer> fieldIds, Integer projectId, String fieldName, String fieldCode, List<Integer> constrainIds, Pageable pageable) {
    return testFieldRepository.findAll(filterByConditions(accountId, fieldIds, projectId, fieldName, fieldCode, constrainIds), pageable);
  }

  private Specification<TestField> filterByConditions(Integer accountId, List<Integer> fieldIds, Integer projectId, String fieldName, String fieldCode, List<Integer> constrainIds) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (fieldIds != null && !fieldIds.isEmpty()) {
        predicates.add(root.get("id").in(fieldIds));
      }
      if (accountId != null) {
        predicates.add(cb.equal(root.get("accountId"), accountId));
      }
      if (projectId != null) {
        predicates.add(cb.equal(root.get("projectId"), projectId));
      }
      if (fieldName != null) {
        predicates.add(cb.like(cb.lower(root.get("fieldName")), "%" + fieldName.toLowerCase() + "%"));
      }
      if (fieldCode != null) {
        predicates.add(cb.like(cb.lower(root.get("fieldCode")), "%" + fieldCode.toLowerCase() + "%"));
      }
      if (constrainIds != null && !constrainIds.isEmpty()) {
        for (Integer constrainId : constrainIds) {

          String jsonStoreTypeId = String.format("[%d]", constrainId);

          predicates.add(cb.isTrue(
              cb.function("JSON_CONTAINS", Boolean.class,
                  root.get("constrainIds"),
                  cb.literal(jsonStoreTypeId)
              )
          ));
        }
      }
      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }

  public List<TestField> findByIdIn(HashSet<Integer> values) {
    return testFieldRepository.findByIdIn(values);
  }

  public boolean existsByFieldCodeAndProjectId(String s, Integer integer) {
    return testFieldRepository.existsByFieldCodeAndProjectId(s, integer);
  }
}
