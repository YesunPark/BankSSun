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

    TOKEN_EXPIRED("유효기간이 만료된 토큰입니다. 다시 로그인해주세요."),
    TOKEN_NOT_MATCH_USER("요청하신 사용자와 Token 인증 사용자가 일치하지 않습니다."),

    ACCOUNT_NOT_FOUND("계좌를 찾을 수 없습니다."),
    SENT_ACCOUNT_NOT_FOUND("송금 보내는 계좌를 찾을 수 없습니다."),
    RECEIVED_ACCOUNT_NOT_FOUND("송금 받는 계좌를 찾을 수 없습니다."),
    ACCOUNT_NOT_EMPTY("계좌에 잔액이 남아있습니다."),
    BALANCE_NOT_ENOUGH("계좌의 잔액이 부족합니다."),
    ;

    private final String description;
}
