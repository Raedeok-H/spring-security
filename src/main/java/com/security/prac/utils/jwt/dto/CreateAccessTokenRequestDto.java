package com.security.prac.utils.jwt.dto;

import lombok.Data;

@Data
public class CreateAccessTokenRequestDto {
    private String refreshToken;
}
