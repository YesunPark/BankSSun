package com.zerobase.BankSSun.service;

import com.zerobase.BankSSun.domain.entity.AccountEntity;
import com.zerobase.BankSSun.domain.repository.AccountRepository;
import com.zerobase.BankSSun.domain.repository.UserRepository;
import com.zerobase.BankSSun.dto.AccountCreateDto;
import com.zerobase.BankSSun.security.TokenProvider;
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
    private final UserRepository userRepository;

    private final TokenProvider tokenProvider;

    /**
     * 계좌 생성_23.08.01
     */
    @Transactional
    public AccountEntity createAccount(String token, AccountCreateDto.Request request) {
        // 토큰에서 추출한 사용자와 요청으로 받은 사용자가 동일한지 비교
        Long tokenUserId = tokenProvider.getId(token);
        if (!Objects.equals(request.getUserId(), tokenUserId)) {
            throw new RuntimeException("요청하신 사용자와 Token 인증 사용자가 일치하지 않습니다.");
        }

        // 맞다면 계좌번호 생성 후 계좌 저장, 저장된 정보 컨트롤러로 넘김
        return accountRepository.save(
            AccountEntity.builder()
                .userId(request.getUserId())
                .accountNumber(makeAccountNumber())
                .accountName("샘플")
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
}
