package com.study.simpleboard.config;

import com.study.simpleboard.config.handler.LogoutSuccessHandler;
import com.study.simpleboard.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // 메서드 보안 활성화
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationSuccessHandler authSuccessHandler;
    private final AuthenticationFailureHandler authFailureHandler;
    private final LogoutSuccessHandler logoutSuccessHandler;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .userDetailsService(customUserDetailsService)
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/api/users/signup", "/api/users/login", "/api/users/login/kakao").permitAll() // 누구나 접근 가능
                        .requestMatchers(HttpMethod.GET, "/api/posts/**", "/api/comments/**").permitAll()  // GET 요청은 모두 허용
                        .requestMatchers("/api/users/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/posts/**", "/api/comments/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/posts/**", "/api/comments/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/posts/**", "/api/comments/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증된 사용자만 접근 가능
                )
                .formLogin(login -> login
                        .loginProcessingUrl("/api/users/login")    // 로그인 처리 URL
                        .usernameParameter("email")                // 로그인 시 사용할 이메일 파라미터 이름
                        .passwordParameter("password")             // 로그인 시 사용할 비밀번호 파라미터 이름
                        .successHandler(authSuccessHandler)        // 로그인 성공 시 처리를 위한 핸들러
                        .failureHandler(authFailureHandler)        // 로그인 실패 시 처리를 위한 핸들러
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/users/logout")                  // 로그아웃 처리 URL
                        .logoutSuccessHandler(logoutSuccessHandler)      // 로그아웃이 되면
                        .invalidateHttpSession(true)                     // 세션 무효화
                        .clearAuthentication(true)                       // 인증 정보 제거
                        .deleteCookies("JSESSIONID")   // 세션 쿠키 삭제
                )
                .sessionManagement(session -> session // 세션 방식 사용
                        .maximumSessions(1)                      // 동시 접속 제한. 한 계정당 최대 1개의 세션만 허용!
                        .maxSessionsPreventsLogin(false)         // 동일 계정으로 새로운 로그인 시도 시 기존 세션 만료(false)
                        .expiredUrl("/")                         // 세션 만료시 이동할 URL
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt 암호화 방식
    }
}
