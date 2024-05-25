package com.security.prac.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final UserDetailsService userDetailsService;

    // Swagger 문서를 로그인 없이 들어가게 해봤는데,
    // 로그인하지 않고 들어가면 제대로 안나와서 소용이 없는 듯
//    @Bean
//    public WebSecurityCustomizer configure() {
//        return (web) -> web.ignoring()
//                .requestMatchers("/swagger-ui/**");
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/signup", "/user").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
//                        .loginProcessingUrl("/api/login") // 로그인 요청 URL 설정 (기본은 /login 에 POST 요청)
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .csrf(AbstractHttpConfigurer::disable);

        // http.addFilterAt(new CustomFilter(), UsernamePasswordAuthenticationFilter.class);
        // 위의 주석처럼 사용하는 것은, 커스텀 필터를 추가하는 방법이다.
        // 기존에 정해진 스프링의 필터 체인들이 있는데, addFilter[At(), Before(), After()]를 사용하면,
        // At: 해당 필터를 대체, Before: 해당 필터 전에 추가, After: 해당 필터 후에 추가하여 커스텀 필터를 적용할 수 있다.
        // UsernamePasswordAuthenticationFilter는 스프링 시큐리티에서 제공하는 기본 로그인 인증 필터인데, username, password를 기본으로 쓰기때문에
        // email, password 등 다른 필드를 사용할때 사용하면 유용할 것이다.
        // 필터는 이번 커밋에서 구현하지 않고, view에서 email을 username으로 받아오도록 구현되어있다.

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
