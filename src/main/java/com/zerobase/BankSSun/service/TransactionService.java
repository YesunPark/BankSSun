package com.zerobase.BankSSun.service;

import static com.zerobase.BankSSun.type.ErrorCode.ACCOUNT_NOT_FOUND;

import com.zerobase.BankSSun.domain.entity.AccountEntity;
import com.zerobase.BankSSun.domain.entity.TransactionEntity;
import com.zerobase.BankSSun.domain.repository.AccountRepository;
import com.zerobase.BankSSun.domain.repository.TransactionRepository;
import com.zerobase.BankSSun.dto.transaction.DepositDto;
import com.zerobase.BankSSun.exception.CustomException;
import com.zerobase.BankSSun.type.Transaction;
import java.time.LocalDateTime;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    /**
     * 현금 입금_23.08.02
     */
    @Transactional
    public DepositDto.Response deposit(DepositDto.Request request) {
        // 계좌번호 존재(삭제 여부) 확인
        AccountEntity accountEntity = accountRepository
            .findByAccountNumber(request.getAccountNumber())
            .orElseThrow(() -> new CustomException(ACCOUNT_NOT_FOUND));

        if (accountEntity.getIsDeleted()) {
            throw new CustomException(ACCOUNT_NOT_FOUND);
        }

        // 계좌가 있다면 입금 금액만큼 잔액 변경
        accountEntity.setAmount(accountEntity.getAmount() + request.getAmount());

        // 거래 내역 테이블에 거래추가
        transactionRepository.save(
            TransactionEntity.builder()
                .accountId(accountEntity.getId())
                .transaction_type(Transaction.DEPOSIT)
                .amount(request.getAmount())
                .depositName(request.getDepositName())
                .build()
        );

        return DepositDto.Response.builder()
            .accountNumber(request.getAccountNumber())
            .depositName(request.getDepositName())
            .amount(request.getAmount())
            .transacted_at(LocalDateTime.now())
            .build();
    }
}