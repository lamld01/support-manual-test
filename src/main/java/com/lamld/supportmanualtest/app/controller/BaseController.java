package com.lamld.supportmanualtest.app.controller;

import com.lamld.supportmanualtest.server.data.auth.AccountInfo;
import org.springframework.security.core.Authentication;

public class BaseController {

  public AccountInfo getAccountInfo(Authentication authentication) {
    if (authentication == null || authentication.getPrincipal() == null) {
      return null;
    }
    return (AccountInfo) authentication.getPrincipal();
  }
}
