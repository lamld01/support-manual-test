package com.lamld.supportmanualtest.server.data.auth;

import com.lamld.supportmanualtest.server.data.type.SellerRoleEnum;
import com.lamld.supportmanualtest.server.entities.Account;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountInfo implements UserDetails {

  private Integer accountId;
  private String username;
  private SellerRoleEnum role;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public String getPassword() {
    return "";
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  public AccountInfo(Account account) {
    if(account != null) {
      this.accountId = account.getId();
      this.username = account.getUsername();
      this.role = account.getRole();
    }
  }

}
