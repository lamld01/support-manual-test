package com.lamld.supportmanualtest.server.storages;

import com.lamld.supportmanualtest.server.repositories.ProjectRepository;
import com.lamld.supportmanualtest.server.repositories.TestApiRepository;
import com.lamld.supportmanualtest.server.repositories.TestFieldRepository;
import com.lamld.supportmanualtest.server.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaseStorage {

  @Autowired protected AccountRepository accountRepository;
  @Autowired protected ProjectRepository projectRepository;
  @Autowired protected TestFieldRepository testFieldRepository;
  @Autowired protected TestApiRepository testApiRepository;
}
