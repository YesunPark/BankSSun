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
import java.time.LocalDateTime;
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

    UserEntity user = UserEntity.builder()
        .id(1L)
        .username("yesun")
        .phone("01000000000")
        .password("1111")
        .role(String.valueOf(Authority.ROLE_USER))
        .build();

AccountCreateDto.Response response = AccountCreateDto.Response.builder()
    .userId(1L)
    .accountNumber("89300000000")
    .amount(1000L)
    .createdAt(LocalDateTime.now())
    .build();

    @Test
    @DisplayName("계좌 생성 성공 - 토큰, 사용자 id, 계좌 별칭, 초기금액을 받아 생성")
    void successCreateAccount() throws Exception {
        //given
        given(accountService.createAccount(any(), any()))
            .willReturn(response);
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

    @Test
    @DisplayName("계좌 생성 실패 - 헤더에 Authorization 없음")
    void failNotIncludeHeader() throws Exception {
        //given
        given(accountService.createAccount(any(), any()))
            .willReturn(response);
        //when
        //then
        mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    AccountCreateDto.Request.builder()
                        .userId(1L)
                        .accountName("test")
                        .initialBalance(1000L)
                        .build()
                )))
            .andExpect(status().is4xxClientError())
            .andDo(print());
    }

    @Test
    @DisplayName("계좌 생성 실패 - 요청 body 형식 오류(사용자 id 누락)")
    void failInvalidRequestBody() throws Exception {
        //given
        given(accountService.createAccount(any(), any()))
            .willReturn(response);
        //when
        //then
        mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "tokennnnnn")
                .content(objectMapper.writeValueAsString(
                    AccountCreateDto.Request.builder()
                        .accountName("test")
                        .initialBalance(1000L)
                        .build()
                )))
            .andExpect(status().is4xxClientError())
            .andDo(print());
    }
}