package com.study.simpleboard.controller;

import com.study.simpleboard.common.response.ApiResponse;
import com.study.simpleboard.dto.request.*;
import com.study.simpleboard.dto.response.UserResponse;
import com.study.simpleboard.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 회원 가입
    @PostMapping("/signup")
    public ApiResponse<UserResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        UserResponse createdUser = userService.signUp(signUpRequest);
        return ApiResponse.success(createdUser);
    }

    // 로그인
    @PostMapping("/login")
    public ApiResponse<UserResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        UserResponse userResponse = userService.login(loginRequest);
        return ApiResponse.success(userResponse);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        return ApiResponse.success(null); // SecurityConfig에서 처리
    }

    // 단일 정보 조회
    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> findById(@PathVariable Long userId) {
        UserResponse userResponse = userService.findById(userId);
        return ApiResponse.success(userResponse);
    }

    // 정보 수정
    @PatchMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(
            @PathVariable Long userId, @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        UserResponse userResponse = userService.updateUser(userId, updateUserRequest);
        return ApiResponse.success(userResponse);
    }

    // 비밀번호 수정
    @PatchMapping("/{userId}/password")
    public ApiResponse<Void> updatePassword(
            @PathVariable Long userId, @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        userService.updatePassword(userId, updatePasswordRequest);
        return ApiResponse.success(null);
    }

    // 회원 탈퇴
    @DeleteMapping("/{userId}")
    public ApiResponse<Void> deleteUser(
            @PathVariable Long userId, @Valid @RequestBody DeleteUserRequest deleteUserRequest,
            HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        userService.deleteUser(userId, deleteUserRequest.getPassword());
        new SecurityContextLogoutHandler().logout(httpRequest, null, null); // 탈퇴 후 자동 로그아웃 처리
        return ApiResponse.success(null);
    }

}
