package com.security.prac.utils.jwt.service;

import com.security.prac.entity.User;
import com.security.prac.repository.UserRepository;
import com.security.prac.utils.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class JwtService {
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!jwtProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected Refresh Token");
        }
        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected User"));

        return jwtProvider.generateToken(user, Duration.ofMinutes(30)); // accessToken 은 30분이 적당한것 같다
    }
}
