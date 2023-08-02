package com.zerobase.BankSSun.dto.transaction;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

public class DepositDto {

    @Getter
    public static class Request {

        @NotBlank
        private String accountNumber;
        @NotBlank
        private String depositName;
//        @Size(min = 1)
        private Long amount;
    }

    @Getter
    @Builder
    public static class Response {

        private String accountNumber;
        private String depositName;
        private Long amount;
        private LocalDateTime transacted_at;
    }
}
