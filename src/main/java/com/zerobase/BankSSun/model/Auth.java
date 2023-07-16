package com.zerobase.BankSSun.model;

import com.zerobase.BankSSun.persist.entity.UserEntity;
import lombok.Data;

public class Auth {

  @Data
  public static class SignIn {

    private String phone;
    private String password;
  }

  @Data
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
