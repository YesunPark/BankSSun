package com.zerobase.BankSSun.web;

import com.zerobase.BankSSun.domain.entity.UserEntity;
import com.zerobase.BankSSun.dto.SignInDto;
import com.zerobase.BankSSun.dto.SignUpDto.SignUpRequest;
import com.zerobase.BankSSun.dto.SignUpDto.SignUpResponse;
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
     * 회원가입 api_23.07.25
     */
    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest request) {
        UserEntity userEntity = this.userService.signUp(request);
        return ResponseEntity.ok(
            SignUpResponse.builder()
                .id(userEntity.getId())
                .phone(userEntity.getPhone())
                .role(userEntity.getRole())
                .username(userEntity.getUsername())
                .build());
    }


    /**
     * 로그인 api_23.07.26
     */
    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn(@RequestBody SignInDto request) {
        UserEntity user = this.userService.authenticate(request);
        String token = this.tokenProvider.generateToken(user.getPhone(), user.getRole());
        return ResponseEntity.ok(token);
    }

}
