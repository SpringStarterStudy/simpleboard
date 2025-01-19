package com.study.simpleboard.config;

import com.study.simpleboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/api/users/signup", "/api/users/login").permitAll() // 누구나 접근 가능
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증된 사용자만 접근 가능
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/api/users/login")
                        .defaultSuccessUrl("/")
                        .permitAll() // 모두 공개 접근
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true) // 로그아웃 시 세션 무효
                )
                .sessionManagement(session -> session
                        .maximumSessions(1)                      // 동시 접속 제한. 한 계정당 최대 1개의 세션만 허용!
                        .maxSessionsPreventsLogin(false)         // 동일 계정으로 새로운 로그인 시도 시 기존 세션 만료(false)
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt 암호화 방식
    }
}
