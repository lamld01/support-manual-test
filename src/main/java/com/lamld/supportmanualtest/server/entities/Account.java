package com.lamld.supportmanualtest.server.entities;

import com.lamld.supportmanualtest.server.data.type.AccountStatusEnum;
import com.lamld.supportmanualtest.server.data.type.SellerRoleEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(schema = "support-manual-test-db", name = "account")
@AttributeOverrides({
    @AttributeOverride(name = "createdAt", column = @Column(name = "created_at")),
    @AttributeOverride(name = "updatedAt", column = @Column(name = "updated_at"))
})
public class Account extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Size(max = 100)
  @NotNull
  @Column(name = "username", nullable = false, length = 100)
  private String username;

  @Size(max = 200)
  @NotNull
  @Column(name = "password", nullable = false, length = 200)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", length = 50)
  private SellerRoleEnum role = SellerRoleEnum.SUPER_ADMIN;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 50)
  private AccountStatusEnum status = AccountStatusEnum.ACTIVE;

}