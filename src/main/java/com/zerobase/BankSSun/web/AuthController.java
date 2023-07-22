package com.zerobase.BankSSun.web;

import com.zerobase.BankSSun.model.Auth;
import com.zerobase.BankSSun.domain.entity.UserEntity;
import com.zerobase.BankSSun.security.TokenProvider;
import com.zerobase.BankSSun.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    /**
     * 회원가입 api_23.07.16
     */
    @PostMapping("/sign-up")
    public ResponseEntity<UserEntity> signUp(@RequestBody Auth.SignUp request) {
        UserEntity userEntity = this.userService.signUp(request);
        return ResponseEntity.ok(userEntity);
    }
}
