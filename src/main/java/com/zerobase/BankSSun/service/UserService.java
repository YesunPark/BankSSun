package com.zerobase.BankSSun.service;

import static com.zerobase.BankSSun.type.ErrorCode.ALREADY_EXISTS_PHONE;

import com.zerobase.BankSSun.domain.entity.UserEntity;
import com.zerobase.BankSSun.domain.repository.UserRepository;
import com.zerobase.BankSSun.dto.SignInDto;
import com.zerobase.BankSSun.dto.SignUpDto.SignUpRequest;
import com.zerobase.BankSSun.exception.UserException;
import com.zerobase.BankSSun.type.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    // 스프링 시큐리티에서 지원하는 기능 사용 위한 메서드
    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        return this.userRepository.findByPhone(phone)
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다. => " + phone));
    }

    /**
     * 회원가입_23.07.25
     */
    @Transactional
    public UserEntity signUp(SignUpRequest user) {
        boolean exists = this.userRepository.existsByPhone(user.getPhone());
        if (exists) {
            throw new UserException(ALREADY_EXISTS_PHONE);
        }

        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user.toEntity());
    }

    /**
     * 로그인 시 검사_23.07.23
     */
    public UserEntity authenticate(SignInDto user) {
        UserEntity userEntity = this.userRepository.findByPhone(user.getPhone())
            .orElseThrow(() -> new UserException(ALREADY_EXISTS_PHONE));

        if (!this.passwordEncoder.matches(user.getPassword(), userEntity.getPassword())) {
            throw new UserException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        return userEntity;
    }
}


