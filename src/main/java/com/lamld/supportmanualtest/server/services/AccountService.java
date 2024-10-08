package com.lamld.supportmanualtest.server.services;

import com.lamld.supportmanualtest.app.dto.account.AccountCreate;
import com.lamld.supportmanualtest.app.dto.account.AccountSignIn;
import com.lamld.supportmanualtest.app.response.account.AccountResponse;
import com.lamld.supportmanualtest.server.data.auth.AccountInfo;
import com.lamld.supportmanualtest.server.data.auth.TokenInfo;
import com.lamld.supportmanualtest.server.entities.Account;
import com.lamld.supportmanualtest.server.exception.BadRequestException;
import com.lamld.supportmanualtest.server.securities.SecurityService;
import com.lamld.supportmanualtest.server.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService extends BaseService {

  private final SecurityService securityService;
  private final JwtUtils jwtUtils;

  /**
   * Retrieves a seller account by its ID.
   *
   * @param id the ID of the seller account
   * @return the AccountResponse representing the found account
   * @throws BadRequestException if the account is not found
   */
  @Transactional(readOnly = true)
  public AccountResponse findAccountById(Long id) {
    Account account = accountStorage.findById(id)
        .orElseThrow();
    return modelMapper.toAccountResponse(account);
  }

  /**
   * Signs up a new seller account.
   *
   * @param accountCreate the data needed to create the seller account
   * @return the AccountResponse representing the created account
   * @throws BadRequestException if the username already exists
   */
  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
  public AccountResponse signUp(AccountCreate accountCreate) {
    boolean isUsernameExist = accountStorage.existsByUsername(accountCreate.getUsername());
    if (isUsernameExist) {
      throw new BadRequestException("Username already exists");
    }
    Account newAccount = modelMapper.toAccount(accountCreate);
    newAccount.setPassword(securityService.encode(accountCreate.getPassword()));
    newAccount = accountStorage.save(newAccount);
    return modelMapper.toAccountResponse(newAccount);
  }

  /**
   * Signs in a seller account and generates a JWT token.
   *
   * @param accountSignIn the sign-in credentials
   * @return TokenInfo containing access token and related information
   * @throws BadRequestException if the username or password is invalid
   */
  @Transactional(readOnly = true)
  public TokenInfo signIn(AccountSignIn accountSignIn) {
    Account account = accountStorage.findByUsername(accountSignIn.getUsername())
        .orElseThrow(() -> new BadRequestException("Invalid username or password"));

    if (!securityService.decode(accountSignIn.getPassword(), account.getPassword())) {
      throw new BadRequestException("Invalid username or password");
    }

    return jwtUtils.createAccessToken(account);
  }


  /**
   * Finds a seller account by its ID.
   *
   * @param accountId the ID of the seller account
   * @return the Account entity representing the found account
   * @throws BadRequestException if the account is not found
   */
  public Account findByAccountId(Long accountId) {
    return accountStorage.findById(accountId)
        .orElseThrow(() -> new BadRequestException("Account not found"));
  }

  public Page<AccountResponse> findSellerAccounts(AccountInfo accountInfo, String username, String status, Pageable pageable) {
    Page<Account> accounts = accountStorage.findByFilters(accountInfo.getAccountId(), username, status, pageable);
    return modelMapper.toPageAccountResponse(accounts);
  }
}
