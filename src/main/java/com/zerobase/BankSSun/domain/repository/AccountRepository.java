package com.zerobase.BankSSun.domain.repository;

import com.zerobase.BankSSun.domain.entity.AccountEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    Optional<AccountEntity> findFirstByOrderByIdDesc();

    Optional<AccountEntity> findByAccountNumber(String accountNumber);
}
