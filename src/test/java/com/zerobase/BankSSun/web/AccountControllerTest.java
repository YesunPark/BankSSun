package com.zerobase.BankSSun.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.BankSSun.domain.entity.AccountEntity;
import com.zerobase.BankSSun.domain.entity.UserEntity;
import com.zerobase.BankSSun.dto.AccountCreateDto;
import com.zerobase.BankSSun.security.TokenProvider;
import com.zerobase.BankSSun.service.AccountService;
import com.zerobase.BankSSun.type.Authority;
import com.zerobase.BankSSun.type.Bank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    @DisplayName("계좌 생성 성공")
    void successCreateAccount() throws Exception {
        //given
        UserEntity user = UserEntity.builder()
            .id(1L)
            .username("yesun")
            .phone("01000000000")
            .password("1111")
            .role(String.valueOf(Authority.ROLE_USER))
            .build();

        given(accountService.createAccount(any(), any()))
            .willReturn(AccountEntity.builder()
                .id(1L)
                .user(user)
                .bank(Bank.SSun)
                .accountNumber("89300000000")
                .accountName("test")
                .amount(1000L)
                .isDeleted(false)
                .build());
        //when
        //then
        mockMvc.perform(post("/account")
                .header(HttpHeaders.AUTHORIZATION, "tokennnnnn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    AccountCreateDto.Request.builder()
                        .userId(1L)
                        .accountName("test")
                        .initialBalance(1000L)
                        .build()
                )))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId").value(1))
            .andExpect(jsonPath("$.accountNumber").value("89300000000"))
            .andDo(print());
    }
}