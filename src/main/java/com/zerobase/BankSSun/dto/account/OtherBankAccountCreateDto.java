package com.zerobase.BankSSun.dto.account;

import com.zerobase.BankSSun.type.Bank;
import java.time.LocalDateTime;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

public class OtherBankAccountCreateDto {

    @Getter
    @Builder
    public static class Request {

        @NotNull(message = "은행명은 필수값입니다.")
        private Bank bank;

        @NotBlank
        @Size(min = 2, message = "계좌 별칭은 2 자리 이상 입력해주세요.")
        private String accountName;

        @NotBlank(message = "계좌번호는 필수값입니다.")
        @Size(min = 11, max = 13, message = "계좌 번호는 11~13 자리입니다.")
        private String accountNumber;

        @NotNull
        @Min(0)
        private Long initialBalance;
    }

    @Getter
    @Builder
    public static class Response {

        private Bank bank;

        private String accountNumber;

        private Long amount;

        private LocalDateTime createdAt;
    }
}
