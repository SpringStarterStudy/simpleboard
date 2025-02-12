package com.study.simpleboard.controller;

import com.study.simpleboard.common.response.ApiResponse;
import com.study.simpleboard.dto.CustomUserDetails;
import com.study.simpleboard.dto.User;
import com.study.simpleboard.dto.request.*;
import com.study.simpleboard.dto.response.UserResponse;
import com.study.simpleboard.service.KakaoUserService;
import com.study.simpleboard.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collections;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final KakaoUserService kakaoUserService;

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.redirect-uri}")
    private String redirectUri;

    // 회원 가입
    @PostMapping("/signup")
    public ApiResponse<UserResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        UserResponse createdUser = userService.signUp(signUpRequest);
        return ApiResponse.success(createdUser);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        return ApiResponse.success(null); // SecurityConfig에서 처리
    }

    // 사용자 정보 조회
    @GetMapping("/me")
    public ApiResponse<UserResponse> getUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserResponse userResponse = userService.findById(userDetails.getUserId());
        return ApiResponse.success(userResponse);
    }

    // 정보 수정
    @PatchMapping("/me")
    public ApiResponse<UserResponse> updateUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        UserResponse userResponse = userService.updateUser(userDetails.getUserId(), updateUserRequest);
        return ApiResponse.success(userResponse);
    }

    // 비밀번호 수정
    @PatchMapping("/me/password")
    public ApiResponse<Void> updatePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        userService.updatePassword(userDetails.getUserId(), updatePasswordRequest);
        return ApiResponse.success(null);
    }

    // 회원 탈퇴
    @DeleteMapping("/me")
    public ApiResponse<Void> deleteUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody DeleteUserRequest deleteUserRequest,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        userService.deleteUser(userDetails.getUserId(), deleteUserRequest.getPassword());
        new SecurityContextLogoutHandler().logout(httpRequest, null, null);
        return ApiResponse.success(null);
    }

    // 카카오 로그인
    @GetMapping("/login/kakao")
    public ApiResponse<UserResponse> kakaoLogin(@RequestParam String code) {
        UserResponse userResponse = kakaoUserService.loginKakaoUser(code);
        return ApiResponse.success(userResponse);
    }

    // 카카오 인증 코드 요청 페이지
    @GetMapping("/oauth/kakao")
    public void redirectToKakaoAuthorization(HttpServletResponse response) throws IOException {
        String kakaoAuthorizationUrl = UriComponentsBuilder.fromHttpUrl("https://kauth.kakao.com/oauth/authorize")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "profile_nickname,account_email") // 필요한 권한 설정
                .build().toUriString();

        response.sendRedirect(kakaoAuthorizationUrl);
    }
}
