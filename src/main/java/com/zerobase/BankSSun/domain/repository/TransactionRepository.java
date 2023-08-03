package com.zerobase.BankSSun.domain.repository;

import com.zerobase.BankSSun.domain.entity.TransactionEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    // 계좌 id, 조회 기간을 정해서 가져오는 것
    List<TransactionEntity> findByAccountIdAndTransactedAtBetween(
        Long accountId, LocalDateTime startDate, LocalDateTime endDate
    );
}
