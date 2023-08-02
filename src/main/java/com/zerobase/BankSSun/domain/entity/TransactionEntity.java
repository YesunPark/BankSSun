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

    private String depositName;

    private String withdrawName;

    private String receivedName;

    private String receivedAccount;

    @CreatedDate
    private LocalDateTime transactedAt;
}
