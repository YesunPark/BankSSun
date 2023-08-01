package com.zerobase.BankSSun.web;

import static com.zerobase.BankSSun.security.JwtAuthenticationFilter.TOKEN_PREFIX;

import com.zerobase.BankSSun.domain.entity.AccountEntity;
import com.zerobase.BankSSun.dto.AccountCreateDto;
import com.zerobase.BankSSun.service.AccountService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /**
     * 계좌 생성_23.08.01
     */
    @PostMapping("/account")
    public ResponseEntity<AccountCreateDto.Response> createAccount(
        @RequestHeader(name = "Authorization") String token,
        @RequestBody @Valid AccountCreateDto.Request request
    ) {
        AccountEntity accountEntity = accountService.createAccount(token.substring(TOKEN_PREFIX.length()), request);
        return ResponseEntity.ok(
            AccountCreateDto.Response.builder()
                .userId(accountEntity.getUserId())
                .accountNumber(accountEntity.getAccountNumber())
                .amount(accountEntity.getAmount())
                .createdAt(accountEntity.getCreatedAt())
                .build()
        );
    }
}
