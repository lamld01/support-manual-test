package com.lamld.supportmanualtest.server.services;

import com.lamld.supportmanualtest.ModelMapper;
import com.lamld.supportmanualtest.server.storages.AccountStorage;
import com.lamld.supportmanualtest.server.storages.ProjectStorage;
import com.lamld.supportmanualtest.server.storages.TestFieldStorage;
import com.lamld.supportmanualtest.server.storages.ValidateConstrainStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaseService {
  @Autowired protected ModelMapper modelMapper;
  @Autowired protected AccountStorage accountStorage;
  @Autowired protected ProjectStorage projectStorage;
  @Autowired protected TestFieldStorage testFieldStorage;
  @Autowired protected ValidateConstrainStorage validateConstrainStorage;
}
