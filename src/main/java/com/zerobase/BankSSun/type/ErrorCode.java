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

    ALREADY_EXISTS_ACCOUNT_NUMBER("이미 존재하는 계좌 번호입니다."),
    ONLY_CAN_REGISTERED_OTHER_BANK("SSun 은행이 아닌 다른 은행의 계좌를 등록해주세요."),
    ACCOUNT_NOT_FOUND("계좌를 찾을 수 없습니다."),
    SENT_ACCOUNT_NOT_FOUND("송금 보내는 계좌를 찾을 수 없습니다."),
    RECEIVED_ACCOUNT_NOT_FOUND("송금 받는 계좌를 찾을 수 없습니다."),
    ACCOUNT_NOT_EMPTY("계좌에 잔액이 남아있습니다."),
    BALANCE_NOT_ENOUGH("계좌의 잔액이 부족합니다."),
    TRANSACTION_TYPE_NOT_FOUND("거래 종류를 확인할 수 없습니다."),

    INVALID_DATE("유효한 날짜인지 확인해주세요."),
    INVALID_DATE_RANGE("거래 내역은 최대 일주일까지 조회할 수 있습니다."),
    NOT_EQUAL_ID_AND_ACCOUNT_NUMBER("accountId 와 accountNumber 의 계좌 id가 일치하지 않습니다."),
    ;

    private final String description;
}