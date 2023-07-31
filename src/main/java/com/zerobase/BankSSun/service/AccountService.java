package com.zerobase.BankSSun.service;

import static com.zerobase.BankSSun.type.ErrorCode.USER_NOT_FOUND;

import com.zerobase.BankSSun.domain.entity.AccountEntity;
import com.zerobase.BankSSun.domain.entity.UserEntity;
import com.zerobase.BankSSun.domain.repository.AccountRepository;
import com.zerobase.BankSSun.domain.repository.UserRepository;
import com.zerobase.BankSSun.dto.AccountCreateDto;
import com.zerobase.BankSSun.exception.UserException;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    /**
     * 계좌 생성_23.07.31
     */
    @Transactional
    public AccountCreateDto.Response createAccount(Long userId, Long initBalance) {
        // 1. 사용자가 있는지 조회
        // 2. 있다면 계좌번호 생성 후 계좌 저장, 저장된 정보를 response dto 에 담아 넘김
        UserEntity userEntity = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        AccountEntity savedAccount = accountRepository.save(AccountEntity.builder()
            .userId(userEntity.getId())
            .accountNumber(makeAccountNumber())
            .accountName("샘플")
            .amount(initBalance)
            .isDeleted(false)
            .build());

        return AccountCreateDto.Response.builder()
            .userId(savedAccount.getUserId())
            .accountNumber(savedAccount.getAccountNumber())
            .amount(savedAccount.getAmount())
            .createdAt(savedAccount.getCreatedAt())
            .build();
    }


    /**
     * 계좌번호 생성_23.07.31
     */
    private String makeAccountNumber() {
        // 계좌번호 13자리 생성(893-XXXXXX-XXXXX)
        // 원래 랜덤으로 생성 예정이었는데, 계좌테이블 가장 마지막 정보를 가져오고
        // 거기서 +1 한 숫자로 생성하는 것도 괜찮은 듯
        String number = "8934567890123";
        // 중복 확인
        if (accountRepository.existsByAccountNumber(number)) { // 계좌번호가 이미 존재하는 경우
            makeAccountNumber();
        }
        return number;
    }
}
