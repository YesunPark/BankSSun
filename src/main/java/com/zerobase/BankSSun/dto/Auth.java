package com.zerobase.BankSSun.dto;

import com.zerobase.BankSSun.domain.entity.UserEntity;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class Auth {

    @Data
    public static class SignIn {

        private String phone;
        private String password;
    }


}
