package com.zerobase.BankSSun.service;

import static com.zerobase.BankSSun.type.ErrorCode.ACCOUNT_NOT_FOUND;
import static com.zerobase.BankSSun.type.ErrorCode.BALANCE_NOT_ENOUGH;
import static com.zerobase.BankSSun.type.ErrorCode.RECEIVED_ACCOUNT_NOT_FOUND;
import static com.zerobase.BankSSun.type.ErrorCode.SENT_ACCOUNT_NOT_FOUND;
import static com.zerobase.BankSSun.type.ErrorCode.TOKEN_NOT_MATCH_USER;
import static com.zerobase.BankSSun.type.ErrorCode.USER_NOT_FOUND;

import com.zerobase.BankSSun.domain.entity.AccountEntity;
import com.zerobase.BankSSun.domain.entity.TransactionEntity;
import com.zerobase.BankSSun.domain.entity.UserEntity;
import com.zerobase.BankSSun.domain.repository.AccountRepository;
import com.zerobase.BankSSun.domain.repository.TransactionRepository;
import com.zerobase.BankSSun.domain.repository.UserRepository;
import com.zerobase.BankSSun.dto.transaction.DepositDto;
import com.zerobase.BankSSun.dto.transaction.RemittanceDto;
import com.zerobase.BankSSun.dto.transaction.WithdrawDto;
import com.zerobase.BankSSun.exception.CustomException;
import com.zerobase.BankSSun.security.TokenProvider;
import com.zerobase.BankSSun.type.Transaction;
import java.time.LocalDateTime;
import java.util.Objects;
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
    private final UserRepository userRepository;

    private final TokenProvider tokenProvider;

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

    /**
     * 현금 출금_23.08.02
     */
    @Transactional
    public WithdrawDto.Response withdraw(String token, WithdrawDto.Request request) {
        // 출금 요청한 계좌의 존재 여부 확인
        Long tokenUserId = tokenProvider.getId(token);
        AccountEntity accountEntity = accountRepository.findByAccountNumber(
                request.getAccountNumber())
            .orElseThrow(() -> new CustomException(ACCOUNT_NOT_FOUND));

        if (accountEntity.getIsDeleted()) {
            throw new CustomException(ACCOUNT_NOT_FOUND);
        }

        // 토큰의 사용자와 출금 요청 계좌의 소유주 일치 여부 확인
        if (!Objects.equals(tokenUserId, accountEntity.getUserId())) {
            throw new CustomException(TOKEN_NOT_MATCH_USER);
        }

        // (출금 요청 금액 > 잔액)의 경우 예외 발생
        if (request.getAmount() > accountEntity.getAmount()) {
            throw new CustomException(BALANCE_NOT_ENOUGH);
        }

        // 출금 요청한 금액만큼 잔액 변경
        accountEntity.setAmount(accountEntity.getAmount() - request.getAmount());

        // 거래 내역 테이블에 거래 추가
        transactionRepository.save(
            TransactionEntity.builder()
                .accountId(accountEntity.getId())
                .transaction_type(Transaction.WITHDRAW)
                .amount(request.getAmount())
                .depositName(request.getWithdrawName())
                .build()
        );

        return WithdrawDto.Response.builder()
            .accountNumber(request.getAccountNumber())
            .withdrawName(request.getWithdrawName())
            .amount(request.getAmount())
            .transacted_at(LocalDateTime.now())
            .build();
    }

    /**
     * 송금_23.08.02
     */
    @Transactional
    public RemittanceDto.Response remittance(String token, RemittanceDto.Request request) {
        AccountEntity sentAccountEntity = accountRepository.findByAccountNumber(
                request.getSentAccount())
            .orElseThrow(() -> new CustomException(SENT_ACCOUNT_NOT_FOUND));

        AccountEntity recievedAccountEntity = accountRepository.findByAccountNumber(
                request.getReceivedAccount())
            .orElseThrow(() -> new CustomException(RECEIVED_ACCOUNT_NOT_FOUND));

        UserEntity recievedUserEntity = userRepository.findById(recievedAccountEntity.getUserId())
            .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        // 토큰의 사용자와 보내는 계좌의 사용자 확인
        Long tokenUserId = tokenProvider.getId(token);

        if (!Objects.equals(tokenUserId, sentAccountEntity.getUserId())) {
            throw new CustomException(TOKEN_NOT_MATCH_USER);
        }

        // 보내는 계좌와 받는 계좌 존재/삭제 여부 확인
        if (sentAccountEntity.getIsDeleted()) {
            throw new CustomException(SENT_ACCOUNT_NOT_FOUND);
        } else if (recievedAccountEntity.getIsDeleted()) {
            throw new CustomException(RECEIVED_ACCOUNT_NOT_FOUND);
        }

        // (송금 요청 금액 > 보내는 계좌의 잔액)의 경우 예외 발생
        if (request.getAmount() > sentAccountEntity.getAmount()) {
            throw new CustomException(BALANCE_NOT_ENOUGH);
        }

        // 보내는 계좌, 받는 계좌의 잔액 변경
        sentAccountEntity.setAmount(sentAccountEntity.getAmount() - request.getAmount());
        recievedAccountEntity.setAmount(recievedAccountEntity.getAmount() + request.getAmount());

        // 거래 테이블에 거래 저장
        transactionRepository.save(
            TransactionEntity.builder()
                .accountId(sentAccountEntity.getId())
                .transaction_type(Transaction.REMITTANCE)
                .amount(request.getAmount())
                .receivedName(recievedUserEntity.getUsername())
                .receivedAccount(request.getReceivedAccount())
                .build()
        );

        return RemittanceDto.Response.builder()
            .sentAccount(request.getSentAccount())
            .receivedAccount(request.getReceivedAccount())
            .receivedBank(recievedAccountEntity.getBank())
            .receivedName(recievedUserEntity.getUsername())
            .amount(request.getAmount())
            .build();
    }

    // 토큰에서 추출한 사용자와 요청 객체에서 추출한 사용자가 일치한지
    // 확인하는 private 메소드 만들기!
}