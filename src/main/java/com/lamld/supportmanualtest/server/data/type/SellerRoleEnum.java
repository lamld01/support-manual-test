package com.lamld.supportmanualtest.server.data.type;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SellerRoleEnum {
  SUPER_ADMIN("Super Admin"),
  ADMIN("Admin"),
  READ_ONLY("Read Only");

  private final String value;
}
