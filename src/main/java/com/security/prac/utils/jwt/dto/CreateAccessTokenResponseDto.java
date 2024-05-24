package com.security.prac.utils.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateAccessTokenResponseDto {
    private String accessToken;
}
