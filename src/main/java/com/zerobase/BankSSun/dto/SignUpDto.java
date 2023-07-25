package com.zerobase.BankSSun.dto;

import com.zerobase.BankSSun.domain.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class SignUpDto {

    @Getter
    @Setter // setter 가 있어야 회원정보 저장 가능
    @Builder
    public static class SignUpRequest {

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

    @Builder
    @Getter
    public static class SignUpResponse {

        private Long id;
        private String username;
        private String phone;
        private String role;
    }
}
