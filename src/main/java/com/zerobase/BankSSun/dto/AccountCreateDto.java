package com.zerobase.BankSSun.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

public class AccountCreateDto {

    @Getter
    @Builder
    public static class Request {

        @NotNull
        @Min(1)
        private Long userId;
        @NotNull
        @Min(0)
        private Long initialBalance;
    }

    @Getter
    @Builder
    public static class Response {

        private Long userId;
        private String accountNumber;
        private Long amount;
        private LocalDateTime createdAt;
    }
}
