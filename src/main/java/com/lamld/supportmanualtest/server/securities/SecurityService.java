package com.lamld.supportmanualtest.server.securities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService implements UserDetailsService {

  @Autowired
  private PasswordEncoder encoder;

  @Override
  public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
    return null;
  }

  public String encode(String password) {
    return encoder.encode(password);
  }

  public boolean decode(String rawPassword, String encodedPassword) {
    return encoder.matches(rawPassword, encodedPassword);
  }
}

