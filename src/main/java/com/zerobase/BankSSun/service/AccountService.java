package com.zerobase.BankSSun.service;

import static com.zerobase.BankSSun.type.ErrorCode.ACCOUNT_NOT_EMPTY;
import static com.zerobase.BankSSun.type.ErrorCode.ACCOUNT_NOT_FOUND;
import static com.zerobase.BankSSun.type.ErrorCode.TOKEN_NOT_MATCH_USER;
import static com.zerobase.BankSSun.type.ErrorCode.USER_NOT_PERMITTED;

import com.zerobase.BankSSun.domain.entity.AccountEntity;
import com.zerobase.BankSSun.domain.repository.AccountRepository;
import com.zerobase.BankSSun.dto.AccountCreateDto;
import com.zerobase.BankSSun.dto.AccountDeleteRequest;
import com.zerobase.BankSSun.exception.CustomException;
import com.zerobase.BankSSun.security.TokenProvider;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final TokenProvider tokenProvider;

    /**
     * 계좌 생성_23.08.01
     */
    @Transactional
    public AccountEntity createAccount(String token, AccountCreateDto.Request request) {
        // 토큰에서 추출한 사용자와 요청으로 받은 사용자가 동일한지 비교
        Long tokenUserId = tokenProvider.getId(token);
        if (!Objects.equals(request.getUserId(), tokenUserId)) {
            throw new CustomException(TOKEN_NOT_MATCH_USER);
        }

        String newAccountNumber = makeAccountNumber();
        String newAccountName = request.getAccountName();

        // 맞다면 계좌번호 생성 후 계좌 저장, 저장된 정보 컨트롤러로 넘김
        return accountRepository.save(
            AccountEntity.builder()
                .userId(request.getUserId())
                .accountNumber(newAccountNumber)
                .accountName(newAccountName == null ? newAccountNumber : newAccountName)
                .amount(request.getInitialBalance())
                .isDeleted(false)
                .build()
        );
    }


    /**
     * 계좌번호 생성_23.08.01
     */
    private String makeAccountNumber() {
        // 계좌번호 11~13자리 생성(초기값: 893-0000-0000)
        // 계좌테이블 가장 마지막으로 생성된 계좌번호 +1 한 숫자 생성
        return accountRepository.findFirstByOrderByIdDesc()
            .map(accountEntity -> (Long.parseLong(accountEntity.getAccountNumber()) + 1) + "")
            .orElse("89300000000");
    }

    /**
     * 계좌 삭제(논리적 삭제)_23.08.01
     */
    @Transactional
    public String deleteAccount(String token, AccountDeleteRequest request) {
        AccountEntity accountEntity = accountRepository.findById(request.getAccountId())
            .orElseThrow(() -> new CustomException(ACCOUNT_NOT_FOUND));

        // 토큰의 사용자 id와 삭제를 요청한 계좌의 userId 비교
        Long tokenUserId = tokenProvider.getId(token);
        if (!Objects.equals(tokenUserId, accountEntity.getUserId())) {
            throw new CustomException(USER_NOT_PERMITTED);
        }

        // 계좌의 잔액이 0원인지 확인, 0원이 아니면 예외발생
        if (accountEntity.getAmount() != 0) {
            throw new CustomException(ACCOUNT_NOT_EMPTY);
        }

        // 계좌 삭제(물리적 삭제가 아닌 삭제 상태로 변경)
        accountEntity.setIsDeleted(true);
        accountEntity.setDeletedAt(LocalDateTime.now());

        return accountEntity.getAccountNumber() + " 계좌가 삭제되었습니다.";
    }
}
