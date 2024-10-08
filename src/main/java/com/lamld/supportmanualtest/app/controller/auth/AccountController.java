package com.lamld.supportmanualtest.app.controller.auth;

import com.lamld.supportmanualtest.app.controller.BaseController;
import com.lamld.supportmanualtest.app.response.BaseResponseDto;
import com.lamld.supportmanualtest.app.response.PageResponse;
import com.lamld.supportmanualtest.app.response.account.AccountResponse;
import com.lamld.supportmanualtest.server.services.AccountService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/manual-test/accounts")
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
public class AccountController extends BaseController {

  private final AccountService accountService;
  @GetMapping("page")
  public BaseResponseDto<PageResponse<AccountResponse>> getAccountPage(Authentication authentication,
                                                                       @RequestParam(required = false) String username,
                                                                       @RequestParam(required = false) String status,
                                                                       @Parameter(hidden = true) @PageableDefault(size = 20) Pageable pageable) {
    Page<AccountResponse> pageResponse = accountService.findSellerAccounts(getAccountInfo(authentication), username, status, pageable);
    return new BaseResponseDto<>(PageResponse.createFrom(pageResponse));
  }
}
