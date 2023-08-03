package com.zerobase.BankSSun.vo;

import com.zerobase.BankSSun.type.Transaction;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TransactionVo {

    private Long id;

    private String transactionTargetName;

    private Long amount;

    private Transaction type;

    private LocalDateTime transactedAt;
}
