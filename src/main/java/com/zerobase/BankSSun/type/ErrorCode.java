package com.zerobase.BankSSun.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    ALREADY_EXISTS_PHONE("이미 가입된 휴대전화번호입니다."),
    PASSWORD_NOT_MATCH("비밀번호가 일치하지 않습니다."),

    USER_NOT_PERMITTED("사용자 권한이 없습니다."),

    TOKEN_EXPIRED("유효기간이 만료된 토큰입니다. 다시 로그인해주세요.");

    private final String description;
}
