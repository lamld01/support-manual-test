package com.lamld.supportmanualtest.server.storages;

import com.lamld.supportmanualtest.server.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaseStorage {

  @Autowired protected AccountRepository accountRepository;
  @Autowired protected ProjectRepository projectRepository;
  @Autowired protected TestFieldRepository testFieldRepository;
  @Autowired protected TestApiRepository testApiRepository;
  @Autowired protected ValidateConstrainRepository validateConstrainRepository;
}
