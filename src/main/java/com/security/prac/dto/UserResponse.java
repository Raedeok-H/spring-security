package com.security.prac.dto;

import com.security.prac.entity.User;
import lombok.Getter;

@Getter
public class UserResponse {
    private final String email;
    private final String password = null;

    public UserResponse(User user) {
        this.email = user.getEmail();
    }
}
