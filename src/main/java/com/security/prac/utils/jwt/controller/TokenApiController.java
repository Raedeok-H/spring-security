package com.security.prac.utils.jwt.controller;

import com.security.prac.utils.jwt.dto.CreateAccessTokenRequestDto;
import com.security.prac.utils.jwt.dto.CreateAccessTokenResponseDto;
import com.security.prac.utils.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenApiController {
    private final JwtService jwtService;

    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponseDto> createAccessToken(@RequestBody CreateAccessTokenRequestDto requestDto){
        String newAccessToken = jwtService.createNewAccessToken(requestDto.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAccessTokenResponseDto(newAccessToken));
    }

}
