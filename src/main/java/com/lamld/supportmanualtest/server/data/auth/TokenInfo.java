package com.lamld.supportmanualtest.server.data.auth;

import com.lamld.supportmanualtest.server.data.type.AccountStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenInfo {

  private String accessToken;

  private Long expiresIn;

  private String refreshToken;

  private String scope;

  private String tokenType = "Barrier";

  private AccountStatusEnum status;

  public TokenInfo(String accessToken, Long expiresIn, String refreshToken, AccountStatusEnum status) {
    this.accessToken = accessToken;
    this.expiresIn = expiresIn;
    this.refreshToken = refreshToken;
    this.status = status;
  }
}
