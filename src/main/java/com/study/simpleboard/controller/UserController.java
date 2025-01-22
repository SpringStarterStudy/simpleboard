package com.study.simpleboard.controller;

import com.study.simpleboard.common.exception.CustomException;
import com.study.simpleboard.common.exception.ErrorCode;
import com.study.simpleboard.common.response.ApiResponse;
import com.study.simpleboard.dto.LoginType;
import com.study.simpleboard.dto.User;
import com.study.simpleboard.dto.request.LoginRequest;
import com.study.simpleboard.dto.request.SignUpRequest;
import com.study.simpleboard.dto.request.UpdatePasswordRequest;
import com.study.simpleboard.dto.request.UpdateUserRequest;
import com.study.simpleboard.dto.response.LoginResponse;
import com.study.simpleboard.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 회원 가입
    @PostMapping("/signup")
    public ApiResponse<User> signUp(@Valid @RequestBody SignUpRequest request) {
        try {
            User userDTO = User.builder()
                    .email(request.getEmail())
                    .password(request.getPassword())
                    .name(request.getName())
                    .cellPhone(request.getCellPhone())
                    .isEnabled(true) // 기본값
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .loginType(LoginType.LOCAL) // 일반 회원가입의 경우 LOCAL로 설정
                    .build();

            User createdUser = userService.signUp(userDTO);
            return ApiResponse.success(createdUser);
        } catch (CustomException e) {
            return ApiResponse.error(e.getErrorCode());
        }
    }

    // 로그인
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse loginResponse = userService.login(request);
            return ApiResponse.success(loginResponse);
        } catch (CustomException e) {
            return ApiResponse.error(e.getErrorCode());
        }
    }

    // 로그아웃
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        try {
            return ApiResponse.success(null); // SecurityConfig에서 처리되므로 여기에서는 성공 응답만 반환하기
        } catch (Exception e) {
            return ApiResponse.error(ErrorCode.LOGOUT_FAILED);
        }
    }

    // 단일 조회
    @GetMapping("/{userId}")
    public ApiResponse<User> findById(@PathVariable Long userId) {
        try {
            User user = userService.findById(userId);
            return ApiResponse.success(user);
        } catch (CustomException e) {
            return ApiResponse.error(e.getErrorCode());
        }
    }

    // 정보 수정
    @PatchMapping("/{userId}")
    public ApiResponse<User> updateUser(
            @PathVariable Long userId, @Valid @RequestBody UpdateUserRequest request) {
        try {
            User updatedUser = userService.updateUser(userId, request);
            return ApiResponse.success(updatedUser);
        } catch (CustomException e) {
            return ApiResponse.error(e.getErrorCode());
        }
    }

    // 비밀번호 수정
    @PatchMapping("/{userId}/password")
    public ApiResponse<Void> updatePassword(
            @PathVariable Long userId, @Valid @RequestBody UpdatePasswordRequest request) {
        try {
            userService.updatePassword(userId, request);
            return ApiResponse.success(null);
        } catch (CustomException e) {
            return ApiResponse.error(e.getErrorCode());
        }
    }

}
