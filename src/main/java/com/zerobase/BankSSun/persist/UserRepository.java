package com.zerobase.BankSSun.persist;

import com.zerobase.BankSSun.persist.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByEmail(String email); // 이메일을 기준으로 회원 정보 찾음

  boolean existsByEmail(String email); // 회원가입 시 이미 존재하는 회원인지 확인

}
