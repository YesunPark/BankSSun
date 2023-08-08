package com.zerobase.BankSSun.service;

import com.zerobase.BankSSun.domain.entity.AccountEntity;
import com.zerobase.BankSSun.domain.entity.UserEntity;
import com.zerobase.BankSSun.domain.repository.AccountRepository;
import com.zerobase.BankSSun.security.TokenProvider;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Spy
    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TokenProvider tokenProvider;

    String token = "token";
    AccountEntity accountEntity = AccountEntity.builder()
        .id(1L)
        .user(UserEntity.builder()
            .id(2L)
            .build())
        .accountNumber("12345")
        .accountName("test")
        .amount(0L)
        .build();
}