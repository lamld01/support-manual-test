package com.lamld.supportmanualtest.server.repositories;

import com.lamld.supportmanualtest.server.entities.ValidateConstrain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ValidateConstrainRepository extends JpaRepository<ValidateConstrain, Integer>, JpaSpecificationExecutor<ValidateConstrain> {
  Optional<ValidateConstrain> findByConstrainName(String name);

  List<ValidateConstrain> findByIdIn(List<Integer> allValidateConstrainIds);
}