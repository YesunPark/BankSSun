package com.zerobase.BankSSun.service;

import static com.zerobase.BankSSun.type.ErrorCode.ACCOUNT_NOT_EMPTY;
import static com.zerobase.BankSSun.type.ErrorCode.USER_NOT_PERMITTED;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.zerobase.BankSSun.domain.entity.AccountEntity;
import com.zerobase.BankSSun.domain.entity.UserEntity;
import com.zerobase.BankSSun.domain.repository.AccountRepository;
import com.zerobase.BankSSun.dto.AccountDeleteRequest;
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
    // 1. 왜 Autowired + MockBean 쓰면 주입을 해줬는데 null 뜨지?
    // java.lang.NullPointerException: Cannot invoke "com.zerobase.BankSSun.domain.repository
    // .AccountRepository.findById(Object)" because "this.accountRepository" is null
    // 2. ExtendWith 에는 Transactional 을 붙이는 건가

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
    AccountDeleteRequest deleteRequest = AccountDeleteRequest.builder()
        .accountId(1L)
        .build();

    @Test
    @DisplayName("계좌 삭제 성공 - 토큰, 삭제할 계좌 id 받음")
    void successDeleteAccount() {
        //given
        given(tokenProvider.getId(token)).willReturn(2L);
        given(accountRepository.findById(deleteRequest.getAccountId()))
            .willReturn(Optional.ofNullable(accountEntity));

        //when
        String result = accountService.deleteAccount(token, deleteRequest);

        //then
        then(accountService).should(times(1)).deleteAccount(token,deleteRequest);
        assertThat(result).isEqualTo("12345 계좌가 삭제되었습니다.");
    }


    @Test
    @DisplayName("계좌 삭제 실패 - 삭제할 계좌의 사용자와 요청한 사용자가 다름")
    void failDeleteAccountUserNotMatch() {
        //given
        given(tokenProvider.getId(token)).willReturn(1L);
        given(accountRepository.findById(deleteRequest.getAccountId()))
            .willReturn(Optional.ofNullable(accountEntity));

        //then
        assertThatThrownBy(() -> accountService.deleteAccount(token, deleteRequest))
            .isExactlyInstanceOf(CustomException.class)
            .hasMessage(USER_NOT_PERMITTED.getDescription());
        then(accountService).should().deleteAccount(token, deleteRequest);
    }

    @Test
    @DisplayName("계좌 삭제 실패 - 계좌에 잔액이 남아있음")
    void failDeleteAccountNotEmpty() {
        //given
        AccountEntity accountEntity = AccountEntity.builder()
            .id(1L)
            .user(UserEntity.builder()
                .id(2L)
                .build())
            .amount(1000L)
            .build();

        given(tokenProvider.getId(token)).willReturn(2L);
        given(accountRepository.findById(deleteRequest.getAccountId()))
            .willReturn(Optional.ofNullable(accountEntity));

        //then
        assertThatThrownBy(() -> accountService.deleteAccount(token, deleteRequest))
            .isExactlyInstanceOf(CustomException.class)
            .hasMessage(ACCOUNT_NOT_EMPTY.getDescription());
        then(accountService).should(times(1)).deleteAccount(token, deleteRequest);
    }
}