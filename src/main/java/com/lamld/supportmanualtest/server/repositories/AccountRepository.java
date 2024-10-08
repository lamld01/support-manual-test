package com.lamld.supportmanualtest.server.repositories;


import com.lamld.supportmanualtest.server.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {

  Optional<Account> findByUsername(String username);

  boolean existsByUsername(String username);
}