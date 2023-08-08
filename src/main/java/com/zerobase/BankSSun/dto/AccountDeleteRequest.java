package com.zerobase.BankSSun.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountDeleteRequest {

    private Long accountId;
}
