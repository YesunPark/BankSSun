package com.zerobase.BankSSun.dto.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.zerobase.BankSSun.type.Transaction;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TransactionDto {

    private Long id;

    private String transactionTargetName;

    private Long amount;

    private Transaction type;

    @JsonFormat(shape = Shape.STRING,  pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime transactedAt;
}