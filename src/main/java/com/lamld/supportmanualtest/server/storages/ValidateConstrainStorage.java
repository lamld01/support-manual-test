package com.lamld.supportmanualtest.server.storages;

import com.lamld.supportmanualtest.server.data.type.StatusEnum;
import com.lamld.supportmanualtest.server.entities.Project;
import com.lamld.supportmanualtest.server.entities.ValidateConstrain;
import jakarta.persistence.criteria.Predicate;
import jodd.bean.BeanWalker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ValidateConstrainStorage extends BaseStorage {

  public Optional<ValidateConstrain> findByConstrainName(String name) {
    return validateConstrainRepository.findByConstrainName(name);
  }

  public ValidateConstrain save(ValidateConstrain validateConstrain) {
    return validateConstrainRepository.saveAndFlush(validateConstrain);
  }

  public Optional<ValidateConstrain> findById(Integer id) {
    return validateConstrainRepository.findById(id);
  }

  public void delete(ValidateConstrain validateConstrain) {
    validateConstrainRepository.delete(validateConstrain);
  }

  public Page<ValidateConstrain> findPage(String constrainName, String regexValue, StatusEnum status, Pageable pageable) {
    return validateConstrainRepository.findAll(findByFilters(constrainName, regexValue, status), pageable);
  }

  private Specification<ValidateConstrain> findByFilters(String constrainName, String regexValue, StatusEnum status) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (constrainName != null && !constrainName.isEmpty()) {
        predicates.add(cb.like(cb.lower(root.get("constrainName")), "%" + constrainName.toLowerCase() + "%"));
      }
      if (regexValue != null&& !regexValue.isEmpty()) {
        predicates.add(cb.like(cb.lower(root.get("regexValue")), "%" + regexValue.toLowerCase() + "%"));
      }
      if (status != null) {
        predicates.add(cb.equal(root.get("status"), status));
      }
      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }

  public List<ValidateConstrain> findAll() {
    return validateConstrainRepository.findAll();
  }

  public List<ValidateConstrain> findByIdIn(List<Integer> allValidateConstrainIds) {
    return validateConstrainRepository.findByIdIn(allValidateConstrainIds);
  }
}
