package com.zerobase.BankSSun.web;

import static com.zerobase.BankSSun.security.JwtAuthenticationFilter.TOKEN_PREFIX;

import com.zerobase.BankSSun.dto.transaction.DepositDto;
import com.zerobase.BankSSun.dto.transaction.WithdrawDto;
import com.zerobase.BankSSun.service.TransactionService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * 현금 입금_23.08.02
     */
    @PutMapping("/deposit")
    public ResponseEntity<DepositDto.Response> deposit(
        @RequestBody @Valid DepositDto.Request request
    ) {
        DepositDto.Response response = transactionService.deposit(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 현금 출금_23.08.02
     */
    @PutMapping("/withdraw")
    public ResponseEntity<WithdrawDto.Response> withdraw(
        @RequestHeader(name = "Authorization") String token,
        @RequestBody @Valid WithdrawDto.Request request
    ) {
        WithdrawDto.Response response = transactionService.withdraw(
            token.substring(TOKEN_PREFIX.length()), request);
        return ResponseEntity.ok(response);
    }
}