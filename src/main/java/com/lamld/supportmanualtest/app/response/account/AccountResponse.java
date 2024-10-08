package com.lamld.supportmanualtest.app.response.account;


import com.lamld.supportmanualtest.server.data.type.AccountStatusEnum;
import com.lamld.supportmanualtest.server.data.type.SellerRoleEnum;
import com.lamld.supportmanualtest.server.entities.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    private Integer id;
    private String username;
    private SellerRoleEnum role;
    private AccountStatusEnum status;
}
