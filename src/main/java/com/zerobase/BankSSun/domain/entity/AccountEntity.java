package com.zerobase.BankSSun.domain.entity;

import com.zerobase.BankSSun.type.Bank;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor // => 필요한 것 맞는가..!
//@NoArgsConstructor
@Entity(name = "ACCOUNT")
@AuditOverride(forClass = BaseEntity.class)
public class AccountEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserEntity user;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Bank bank;

    @NotNull
    private String accountNumber;

    @NotNull
    private String accountName;

    @NotNull
    @Min(value = 0, message = "계좌의 잔액은 0원 이상이어야 합니다.")
    private Long amount;

    @NotNull
    private Boolean isDeleted;

    private LocalDateTime deletedAt;
}
