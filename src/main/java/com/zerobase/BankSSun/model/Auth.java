package com.zerobase.BankSSun.model;

import com.zerobase.BankSSun.domain.entity.UserEntity;
import java.util.List;
import lombok.Builder;
import lombok.Data;

public class Auth {

    @Data
    public static class SignIn {

        private String phone;
        private String password;
    }

    @Data // setter 가 있어야 회원정보 저장 가능
    @Builder
    public static class SignUp {

        private String username;
        private String phone;
        private String password;
        private String role;

        public UserEntity toEntity() {
            return UserEntity.builder()
                .phone(this.phone)
                .username(this.username)
                .password(this.password)
                .role(this.role)
                .build();
        }
    }
}
