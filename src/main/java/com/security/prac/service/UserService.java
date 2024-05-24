package com.security.prac.service;

import com.security.prac.dto.LoginRequest;
import com.security.prac.entity.User;
import com.security.prac.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(LoginRequest dto) {
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .passwordHashed(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build()).getId();
    }
}
