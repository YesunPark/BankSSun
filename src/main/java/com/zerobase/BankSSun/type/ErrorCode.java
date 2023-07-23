package com.zerobase.BankSSun.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND("가입되지 않은 사용자입니다."),
    ALREADY_EXISTS_PHONE("이미 가입된 휴대전화번호입니다."),
    PASSWORD_NOT_MATCH("비밀번호가 일치하지 않습니다.");

    private final String description;
}
