package com.lamld.supportmanualtest.app.controller.external;

import com.lamld.supportmanualtest.app.controller.BaseController;
import com.lamld.supportmanualtest.app.dto.account.AccountCreate;
import com.lamld.supportmanualtest.app.dto.account.AccountSignIn;
import com.lamld.supportmanualtest.app.response.BaseResponseDto;
import com.lamld.supportmanualtest.app.response.account.AccountResponse;
import com.lamld.supportmanualtest.server.data.auth.TokenInfo;
import com.lamld.supportmanualtest.server.services.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("v1/manual-test/public/auth")
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
public class AuthController extends BaseController {

    private final AccountService accountService;

    @PostMapping("/sign-up")
    public BaseResponseDto<AccountResponse> signUp(
            @Valid @RequestBody AccountCreate accountCreate) {
        AccountResponse userAccountResponse = accountService.signUp(accountCreate);
        return new BaseResponseDto<>(userAccountResponse);
    }

    @PostMapping("/sign-in")
    public BaseResponseDto<TokenInfo> signIn(
        @Valid @RequestBody AccountSignIn accountSignIn) {
        TokenInfo tokenInfo = accountService.signIn(accountSignIn);
        return new BaseResponseDto<>(tokenInfo);
    }
}
