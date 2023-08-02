package com.zerobase.BankSSun.dto.transaction;

import com.zerobase.BankSSun.type.Bank;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

public class RemittanceDto {

    @Getter
    public static class Request {

        @NotBlank(message = "보내는 계좌번호는 필수값입니다.")
        private String sentAccount;

        @NotBlank(message = "받는 계좌번호는 필수값입니다.")
        private String receivedAccount;

        @NotNull(message = "송금액은 필수값입니다.")
        @Min(value = 1, message = "송금 최소금액은 1원입니다.")
        private Long amount;
    }

    @Getter
    @Builder
    public static class Response {

        private String sentAccount;

        private String receivedAccount;

        private Bank receivedBank;

        private String receivedName;

        private Long amount;
    }
}