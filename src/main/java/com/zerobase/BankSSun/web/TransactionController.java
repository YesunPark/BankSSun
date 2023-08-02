package com.zerobase.BankSSun.web;

import com.zerobase.BankSSun.dto.transaction.DepositDto;
import com.zerobase.BankSSun.dto.transaction.DepositDto.Response;
import com.zerobase.BankSSun.service.TransactionService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * 현금 입금_23.08.02
     */
    @PutMapping("/deposit")
    public ResponseEntity<Response> deposit(
        @RequestBody @Valid DepositDto.Request request) {
        DepositDto.Response response = transactionService.deposit(request);
        return ResponseEntity.ok(response);
    }
}