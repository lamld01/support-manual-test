package com.lamld.supportmanualtest.server.storages;

import com.lamld.supportmanualtest.server.entities.Account;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class AccountStorage extends BaseStorage {

    public Optional<Account> findById(Long accountId) {
        return accountRepository.findById(accountId);
    }

    public Page<Account> findByFilters(Long managerAccountId, String username, String status, Pageable pageable) {
        return accountRepository.findAll(filterByConditions(managerAccountId, username, status), pageable);
    }

    private Specification<Account> filterByConditions(Long managerAccountId, String username, String status) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (managerAccountId != null) {
                predicates.add(cb.equal(root.get("managerAccountId"), managerAccountId));
            }

            if (username != null) {
                predicates.add(cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%"));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Account save(Account account) {
        return accountRepository.saveAndFlush(account);
    }

    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }
}
