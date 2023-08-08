package com.zerobase.BankSSun.service;

import static com.zerobase.BankSSun.type.ErrorCode.TOKEN_NOT_MATCH_USER;
import static com.zerobase.BankSSun.type.ErrorCode.USER_NOT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.zerobase.BankSSun.domain.entity.AccountEntity;
import com.zerobase.BankSSun.domain.entity.UserEntity;
import com.zerobase.BankSSun.domain.repository.AccountRepository;
import com.zerobase.BankSSun.domain.repository.UserRepository;
import com.zerobase.BankSSun.dto.AccountCreateDto;
import com.zerobase.BankSSun.dto.AccountCreateDto.Response;
import com.zerobase.BankSSun.exception.CustomException;
import com.zerobase.BankSSun.security.TokenProvider;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
    private UserRepository userRepository;

    @Mock
    private TokenProvider tokenProvider;

    String token = "token";
    UserEntity userEntity = UserEntity.builder()
        .id(2L)
        .username("testUser")
        .build();
    AccountEntity accountEntity = AccountEntity.builder()
        .id(1L)
        .user(UserEntity.builder()
            .id(2L)
            .build())
        .accountNumber("12345")
        .accountName("test")
        .amount(0L)
        .build();

    @Test
    @DisplayName("계좌 생성 성공 - 토큰, 사용자 id, 계좌 별칭, 초기금액을 받아 생성")
    void successAccountCreate() {
        // given
        AccountCreateDto.Request request = AccountCreateDto.Request.builder()
            .userId(2L)
            .accountName("testTest")
            .initialBalance(0L)
            .build();

        given(tokenProvider.getId(token)).willReturn(2L);
        given(userRepository.findById(request.getUserId()))
            .willReturn(Optional.ofNullable(userEntity));
        given(accountRepository.save(any())).willReturn(accountEntity);

        // when
        Response response = accountService.createAccount(token, request);

        // then
        then(accountService).should(times(1)).createAccount(token, request);
        assertThat(response.getAccountNumber()).isEqualTo("12345");
        assertThat(response.getUserId()).isEqualTo(2L);
        assertThat(response.getAmount()).isEqualTo(0L);
    }

    @Test
    @DisplayName("계좌 생성 실패 - 토큰의 사용자와 요청으로 받은 사용자 다름")
    void failAccountCreateNotMatchUser() {
        // given
        AccountCreateDto.Request request = AccountCreateDto.Request.builder()
            .userId(1L)
            .accountName("testTest")
            .initialBalance(0L)
            .build();

        given(tokenProvider.getId(token)).willReturn(2L);

        // then
        assertThatThrownBy(() -> accountService.createAccount(token, request))
            .isExactlyInstanceOf(CustomException.class)
            .hasMessage(TOKEN_NOT_MATCH_USER.getDescription());
        then(accountService).should(times(1)).createAccount(token, request);
    }

    @Test
    @DisplayName("계좌 생성 실패 - 존재하지 않는 사용자 id 로 요청")
    void failAccountCreateUserNotFound() {
        // given
        AccountCreateDto.Request request = AccountCreateDto.Request.builder()
            .userId(2L)
            .accountName("testTest")
            .initialBalance(0L)
            .build();

        given(tokenProvider.getId(token)).willReturn(2L);
        given(userRepository.findById(request.getUserId()))
            .willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> accountService.createAccount(token, request))
            .isExactlyInstanceOf(CustomException.class)
            .hasMessage(USER_NOT_FOUND.getDescription());
        then(accountService).should(times(1)).createAccount(token, request);
    }
}