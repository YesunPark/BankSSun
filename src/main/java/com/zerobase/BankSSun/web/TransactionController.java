package com.zerobase.BankSSun.web;

import static com.zerobase.BankSSun.security.JwtAuthenticationFilter.TOKEN_PREFIX;

import com.zerobase.BankSSun.dto.transaction.DepositDto;
import com.zerobase.BankSSun.dto.transaction.RemittanceDto;
import com.zerobase.BankSSun.dto.transaction.TransactionListDto;
import com.zerobase.BankSSun.dto.transaction.WithdrawDto;
import com.zerobase.BankSSun.service.TransactionService;
import java.text.ParseException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    /**
     * 송금_23.08.02
     */
    @PutMapping("/remittance")
    public ResponseEntity<RemittanceDto.Response> remittance(
        @RequestHeader(name = "Authorization") String token,
        @RequestBody @Valid RemittanceDto.Request request
    ) {
        return ResponseEntity.ok(
            transactionService.remittance(token.substring(TOKEN_PREFIX.length()), request)
        );
    }

    /**
     * 거래 내역 조회_23.08.03
     *
     * @return 최소 하루 ~ 최대 일주일의 거래 내역
     * @apiNote 시작 날짜, 끝 날짜 둘 다 null 로 보낼 경우 조회일 포함 일주일 내역 반환
     * @apiNote 날짜 포맷 : yyyy-MM-dd
     */
    @GetMapping("/transaction-list")
    public ResponseEntity<TransactionListDto.Response> getTransactionList(
        @RequestHeader(name = "Authorization") String token,
        @RequestBody @Valid TransactionListDto.Request request
    ) {
        return ResponseEntity.ok(
            transactionService.getTransactionList(token.substring(TOKEN_PREFIX.length()), request)
        );
    }
}