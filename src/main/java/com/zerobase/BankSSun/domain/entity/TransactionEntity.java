package com.zerobase.BankSSun.domain.entity;

import com.zerobase.BankSSun.type.Transaction;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Builder
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity(name = "TRANSACTION")
@EntityListeners(AuditingEntityListener.class)
public class TransactionEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long accountId;

    @Enumerated(EnumType.STRING)
    private Transaction transaction_type;

    @NotNull
    private Long amount;

    private String depositName; // 입금자명

    private String withdrawName; // 출금자명

    private String receivedName; // 송금받는 계좌주명

    private String receivedAccount; // 송금받는 계좌번호

    @CreatedDate
    private LocalDateTime transactedAt;
}
