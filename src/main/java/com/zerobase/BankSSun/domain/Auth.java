package com.zerobase.BankSSun.domain;

import com.zerobase.BankSSun.persist.entity.UserEntity;
import lombok.Data;

public class Auth {

  @Data
  public static class SignIn {

    private String phone;
    private String password;
  }

  @Data // setter 가 있어야 회원정보 저장 가능
  public static class SignUp {

    private String username;
    private String phone;
    private String password;

    public UserEntity toEntity() {
      return UserEntity.builder()
          .phone(this.phone)
          .username(this.username)
          .password(this.password)
          .build();
    }
  }
}
