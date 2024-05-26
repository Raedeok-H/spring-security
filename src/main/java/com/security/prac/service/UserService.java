package com.security.prac.service;

import com.security.prac.dto.LoginRequest;
import com.security.prac.dto.LoginResponseDto;
import com.security.prac.entity.User;
import com.security.prac.repository.UserRepository;
import com.security.prac.utils.jwt.JwtProvider;
import com.security.prac.utils.jwt.entity.RefreshToken;
import com.security.prac.utils.jwt.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public Long save(LoginRequest dto) {
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .passwordHashed(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build()).getId();
    }

    public LoginResponseDto login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!bCryptPasswordEncoder.matches(password, user.getPasswordHashed())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 기존 RefreshToken 찾기
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUserId(user.getId());
        existingToken.ifPresent(refreshTokenRepository::delete); // 기존 토큰이 있으면 삭제

        String accessToken = jwtProvider.generateToken(user, Duration.ofMinutes(30));
        String refreshToken = jwtProvider.generateToken(user, Duration.ofDays(1));
        refreshTokenRepository.save(new RefreshToken(user.getId(), refreshToken));

        return new LoginResponseDto(email, accessToken, refreshToken);
    }

    @Transactional
    public boolean deleteRefreshTokenByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> {
                    refreshTokenRepository.deleteByUserId(user.getId());
                    return true;
                })
                .orElse(false);
    }
}
