package com.zerobase.BankSSun.service;

import com.zerobase.BankSSun.model.Auth;
import com.zerobase.BankSSun.persist.UserRepository;
import com.zerobase.BankSSun.persist.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  // 스프링 시큐리티에서 지원하는 기능 사용 위한 메서드
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return this.userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다. => " + email));
  }

  public UserEntity register(Auth.SignUp user) { // 회원가입
    boolean exists = this.userRepository.existsByEmail(user.getEmail());
    if (exists) {
      throw new RuntimeException("이미 가입된 이메일입니다.");
    }

    user.setPassword(this.passwordEncoder.encode(user.getPassword()));
    return this.userRepository.save(user.toEntity());
  }

  public UserEntity authenticate(Auth.SignIn user) { // 로그인 시 검사 
    return null;
  }
}


