package com.zerobase.BankSSun.domain.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditOverride;

@Builder
@Getter
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

    @NotNull
    private Long userId;

    @NotNull
    private String accountNumber;

    @NotNull
    private String accountName;

    @NotNull
    private Long amount;

    @NotNull
    private Boolean isDeleted;

    private LocalDateTime deletedAt;
}
