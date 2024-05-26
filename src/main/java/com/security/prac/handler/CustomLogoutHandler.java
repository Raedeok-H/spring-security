package com.security.prac.handler;

import com.security.prac.service.UserService;
import com.security.prac.utils.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final JwtProvider jwtProvider;
    private final UserService userService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtProvider.validateToken(token)) {
                String email = jwtProvider.getAuthentication(token).getName();
                boolean success = userService.deleteRefreshTokenByEmail(email);
                request.setAttribute("logoutSuccess", success);
                request.setAttribute("email", email);
            }
        }
    }
}
