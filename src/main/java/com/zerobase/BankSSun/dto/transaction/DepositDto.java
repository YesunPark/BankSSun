package com.zerobase.BankSSun.dto.transaction;

import java.time.LocalDateTime;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

public class DepositDto {

    @Getter
    public static class Request {

        @NotBlank(message = "계좌번호는 필수값입니다.")
        private String accountNumber;
        @NotBlank(message = "입금자명은 필수값입니다.")
        private String depositName;
        @Min(value = 1000, message = "입금 최소금액은 1000원입니다.")
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
