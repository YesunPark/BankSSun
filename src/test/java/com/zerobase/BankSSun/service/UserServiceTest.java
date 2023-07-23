package com.zerobase.BankSSun.service;

import static org.junit.jupiter.api.Assertions.*;

import com.zerobase.BankSSun.domain.entity.UserEntity;
import com.zerobase.BankSSun.model.Auth.SignUp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    /**
     * 회원가입_23.07.23
     */
    @Test
    @DisplayName("회원가입")
    void signUp() {
        //given
        SignUp form = SignUp.builder()
            .phone("01012345678")
            .username("test")
            .password("101010")
            .role("ROLE_USER")
            .build();

        //when
        UserEntity user = userService.signUp(form);

        //then
        assertEquals("01012345678", user.getPhone());
        assertEquals("test", user.getUsername());
    }
}