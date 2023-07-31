package com.zerobase.BankSSun.web;

import com.zerobase.BankSSun.dto.AccountCreateDto;
import com.zerobase.BankSSun.dto.AccountCreateDto.Response;
import com.zerobase.BankSSun.service.AccountService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /**
     * 계좌 생성_23.07.31
     */
    @PostMapping("/account")
    public ResponseEntity<AccountCreateDto.Response> createAccount(
        @RequestBody @Valid AccountCreateDto.Request request
    ) {
        Response response = accountService.createAccount(
            request.getUserId(), request.getInitialBalance()
        );
        return ResponseEntity.ok(response);
    }
}
