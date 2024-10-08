package com.lamld.supportmanualtest.app.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "The class for user login requests")
public class AccountSignIn {

  @Schema(description = "The user's username address")
  private String username;

  @Schema(description = "The user's password")
  private String password;
}
