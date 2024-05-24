package com.security.prac.utils.jwt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.prac.entity.User;
import com.security.prac.jwt.JwtFactory;
import com.security.prac.repository.UserRepository;
import com.security.prac.utils.jwt.JwtProperties;
import com.security.prac.utils.jwt.dto.CreateAccessTokenRequestDto;
import com.security.prac.utils.jwt.entity.RefreshToken;
import com.security.prac.utils.jwt.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TokenApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    JwtProperties jwtProperties;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        refreshTokenRepository.deleteAll();
    }

    @DisplayName("CreateNewAccessToken")
    @Test
    public void createNewAccessToken() throws Exception {
        // given
        final String url = "/api/token";

        User testUser = userRepository.findByEmail("test").orElseThrow(() ->
                new IllegalArgumentException("User not found"));

        String refreshToken = JwtFactory.builder()
                .claims(Map.of("id", testUser.getId()))
                .build()
                .createToken(jwtProperties);

        refreshTokenRepository.save(new RefreshToken(testUser.getId(), refreshToken));

        CreateAccessTokenRequestDto requestDto = new CreateAccessTokenRequestDto();
        requestDto.setRefreshToken(refreshToken);

        final String requestBody = objectMapper.writeValueAsString(requestDto);

        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        resultActions.andExpect(result -> {
            status().isCreated();
            jsonPath("$.accessToken").isNotEmpty();
        });
    }

}