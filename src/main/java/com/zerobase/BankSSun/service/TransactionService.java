package com.zerobase.BankSSun.service;

import static com.zerobase.BankSSun.type.ErrorCode.ACCOUNT_NOT_FOUND;
import static com.zerobase.BankSSun.type.ErrorCode.BALANCE_NOT_ENOUGH;
import static com.zerobase.BankSSun.type.ErrorCode.INVALID_DATE;
import static com.zerobase.BankSSun.type.ErrorCode.INVALID_DATE_RANGE;
import static com.zerobase.BankSSun.type.ErrorCode.NOT_EQUAL_ID_AND_ACCOUNT_NUMBER;
import static com.zerobase.BankSSun.type.ErrorCode.RECEIVED_ACCOUNT_NOT_FOUND;
import static com.zerobase.BankSSun.type.ErrorCode.SENT_ACCOUNT_NOT_FOUND;
import static com.zerobase.BankSSun.type.ErrorCode.TOKEN_NOT_MATCH_USER;

import com.zerobase.BankSSun.domain.entity.AccountEntity;
import com.zerobase.BankSSun.domain.entity.TransactionEntity;
import com.zerobase.BankSSun.domain.repository.AccountRepository;
import com.zerobase.BankSSun.domain.repository.TransactionRepository;
import com.zerobase.BankSSun.dto.transaction.DepositDto;
import com.zerobase.BankSSun.dto.transaction.RemittanceDto;
import com.zerobase.BankSSun.dto.transaction.TransactionDto;
import com.zerobase.BankSSun.dto.transaction.TransactionListDto;
import com.zerobase.BankSSun.dto.transaction.WithdrawDto;
import com.zerobase.BankSSun.exception.CustomException;
import com.zerobase.BankSSun.security.TokenProvider;
import com.zerobase.BankSSun.type.ErrorCode;
import com.zerobase.BankSSun.type.Transaction;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
                .account(accountEntity)
                .transactionType(Transaction.DEPOSIT)
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
        if (!Objects.equals(tokenUserId, accountEntity.getUser().getId())) {
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
                .account(accountEntity)
                .transactionType(Transaction.WITHDRAW)
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
                request.getSentAccountNumber())
            .orElseThrow(() -> new CustomException(SENT_ACCOUNT_NOT_FOUND));

        AccountEntity recievedAccountEntity = accountRepository.findByAccountNumber(
                request.getReceivedAccountNumber())
            .orElseThrow(() -> new CustomException(RECEIVED_ACCOUNT_NOT_FOUND));

        // 토큰의 사용자와 보내는 계좌의 사용자 확인
        Long tokenUserId = tokenProvider.getId(token);

        if (!Objects.equals(tokenUserId, sentAccountEntity.getUser().getId())) {
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
                .account(sentAccountEntity)
                .transactionType(Transaction.REMITTANCE)
                .amount(request.getAmount())
                .receivedName(recievedAccountEntity.getUser().getUsername())
                .receivedAccount(request.getReceivedAccountNumber())
                .build()
        );

        return RemittanceDto.Response.builder()
            .sentAccountNumber(request.getSentAccountNumber())
            .receivedAccountNumber(request.getReceivedAccountNumber())
            .receivedBank(recievedAccountEntity.getBank())
            .receivedName(recievedAccountEntity.getUser().getUsername())
            .amount(request.getAmount())
            .build();
    }

    /**
     * 거래 내역 조회_23.08.03
     *
     * @apiNote 삭제된 계좌는 거래 내역 조회 불가
     * @apiNote 삭제되지 않은 계좌의 거래 내역에 포함된 삭제된 계좌와의 거래는 표시
     */
    @Transactional
    public TransactionListDto.Response getTransactionList
    (
        String token, TransactionListDto.Request request
    ) {
        // 조회 계좌 존재/삭제 여부 확인
        AccountEntity accountEntity = getValidAccountEntity(request.getAccountNumber());

        // 토큰의 사용자 id와 거래내역을 조회할 계좌의 userId 확인
        if (!Objects.equals(tokenProvider.getId(token), accountEntity.getUser().getId())) {
            throw new CustomException(TOKEN_NOT_MATCH_USER);
        }

        // 계좌 id 와 계좌번호의 계좌 id 일치 여부 확인
        TransactionEntity transactionEntity = transactionRepository.findById(request.getAccountId())
            .orElseThrow(() -> new CustomException(ACCOUNT_NOT_FOUND));
        if (!request.getAccountNumber().equals(transactionEntity.getAccount().getAccountNumber())) {
            throw new CustomException(NOT_EQUAL_ID_AND_ACCOUNT_NUMBER);
        }

        // 올바르게 요청된 날짜 형식: "yyyy-MM-dd"
        int DATE_FORMAT_LENGTH = ("yyyy-MM-dd").length();
        String startDateStr = request.getStartDate();
        String endDateStr = request.getEndDate();
        String nowDateStr = LocalDateTime.now().toString().substring(0, DATE_FORMAT_LENGTH);
        int defaultDateRange = 7;
        int maxDateRange = 7;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 시작 날짜와 끝 날짜가 null 인 경우 (조회일 포함 일주일 내역 반환)
        if (startDateStr == null && endDateStr == null) {
            LocalDateTime nowDateTime = LocalDateTime.now();
            String weekAgoStr = nowDateTime.minusDays(defaultDateRange - 1).toString()
                .substring(0, DATE_FORMAT_LENGTH);

            LocalDateTime weekAgoDateTime = LocalDateTime.parse(
                String.format("%s 00:00:00", weekAgoStr), formatter
            );

            return getTransactionListResponse(request.getAccountId(), weekAgoDateTime, nowDateTime);
        }

        // 시작 날짜와 끝 날짜 둘 중 하나라도 null 로 보낸 경우
        if (startDateStr == null || endDateStr == null) {
            throw new CustomException(INVALID_DATE);
        }

        // ======== 이 아래로는 두 날짜 다 null 이 아닌 경우들 ========

        LocalDateTime startDateTime;
        LocalDateTime endDateTime;

        // 두 날짜 중 하나라도 유효하지 않은 날짜(형식)인 경우 ex. 2023-00-44, 230101, 20230808, 23-08-08...
        try {
            startDateTime = LocalDateTime.parse(
                String.format("%s 00:00:00", startDateStr), formatter);
            endDateTime = LocalDateTime.parse(
                String.format("%s 23:59:59", endDateStr), formatter);
        } catch (DateTimeException e) {
            throw new CustomException(INVALID_DATE);
        }

        // 끝 날짜가 시작 날짜를 초과한 경우
        if (startDateTime.isAfter(endDateTime)) {
            throw new CustomException(INVALID_DATE);
        }

        // 끝 날짜가 조회 당일 날짜를 초과한 경우
        LocalDateTime todayDateTime = LocalDateTime.parse(
            String.format("%s 23:59:59", nowDateStr), formatter
        );
        if (endDateTime.isAfter(todayDateTime)) {
            throw new CustomException(INVALID_DATE);
        }

        // 조회 기간이 최대 조회 기간을 넘을 경우
        int betweenDays = Period.between(startDateTime.toLocalDate(), endDateTime.toLocalDate())
            .getDays();
        if (betweenDays + 1 > maxDateRange) {
            throw new CustomException(INVALID_DATE_RANGE);
        }

        // 두 날짜 전부 제대로 조회한 경우
        return getTransactionListResponse(request.getAccountId(), startDateTime, endDateTime);
    }

    // 토큰에서 추출한 사용자와 요청 객체에서 추출한 사용자가 일치한지
    // 확인하는 private 메소드 만들기!

    /**
     * 계좌 존재/삭제 여부 확인_23.08.03
     */
    private AccountEntity getValidAccountEntity(String accountNumber) {
        AccountEntity accountEntity = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new CustomException(ACCOUNT_NOT_FOUND));

        if (accountEntity.getIsDeleted()) {
            throw new CustomException(ACCOUNT_NOT_FOUND);
        }
        return accountEntity;
    }

    /**
     * 거래 종류에 따라 거래 대상자 이름 반환_23.08.03
     *
     * @implNote 반환되는 이름 종류: 출금자명, 입금자명, 송금 받은 계좌 소유주명
     * @implNote getTransactionList 에서만 사용
     */
    private String getTransactionTargetName(TransactionEntity transaction) {
        switch (transaction.getTransactionType()) {
            case WITHDRAW -> {
                return transaction.getWithdrawName();
            }
            case DEPOSIT -> {
                return transaction.getDepositName();
            }
            case REMITTANCE -> {
                return transaction.getReceivedName();
            }
        }
        return ErrorCode.TRANSACTION_TYPE_NOT_FOUND.getDescription();
    }

    /**
     * 거래 내역 조회 Response Dto 반환_23.08.03
     *
     * @implNote getTransactionList 에서만 사용
     */
    private TransactionListDto.Response getTransactionListResponse(
        Long accountId, LocalDateTime startDateTime, LocalDateTime endDateTime
    ) {
        List<TransactionEntity> resultList = transactionRepository.findByAccountIdAndTransactedAtBetween(
            accountId, startDateTime, endDateTime
        );
        return TransactionListDto.Response.builder()
            .transactionList(resultList.stream()
                .map(transaction ->
                    TransactionDto.builder()
                        .id(transaction.getId())
                        .transactionTargetName(getTransactionTargetName(transaction))
                        .amount(transaction.getAmount())
                        .type(transaction.getTransactionType())
                        .transactedAt(transaction.getTransactedAt())
                        .build()
                ).toList())
            .build();
    }
}