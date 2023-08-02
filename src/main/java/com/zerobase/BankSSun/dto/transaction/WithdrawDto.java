package com.zerobase.BankSSun.dto.transaction;

import java.time.LocalDateTime;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

public class WithdrawDto {

    @Getter
    public static class Request {

        @NotBlank
        private String accountNumber;
        @NotBlank
        private String withdrawName;
        @Min(value = 1000, message = "출금 최소금액은 1000원입니다.")
        private Long amount;
    }

    @Getter
    @Builder
    public static class Response {

        private String accountNumber;
        private String withdrawName;
        private Long amount;
        private LocalDateTime transacted_at;
    }
}
