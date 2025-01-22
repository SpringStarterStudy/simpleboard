package com.study.simpleboard.controller;

import com.study.simpleboard.common.exception.CustomException;
import com.study.simpleboard.common.exception.ErrorCode;
import com.study.simpleboard.common.response.ApiResponse;
import com.study.simpleboard.dto.LoginType;
import com.study.simpleboard.dto.UserDTO;
import com.study.simpleboard.dto.request.LoginRequest;
import com.study.simpleboard.dto.request.SignUpRequest;
import com.study.simpleboard.dto.response.LoginResponse;
import com.study.simpleboard.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 회원 가입
    @PostMapping("/signup")
    public ApiResponse<UserDTO> signUp(@Valid @RequestBody SignUpRequest request) {
        try {
            UserDTO userDTO = UserDTO.builder()
                    .email(request.getEmail())
                    .password(request.getPassword())
                    .name(request.getName())
                    .cellPhone(request.getCellPhone())
                    .isEnabled(true) // 기본값
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .loginType(LoginType.LOCAL) // 일반 회원가입의 경우 LOCAL로 설정
                    .build();

            UserDTO createdUser = userService.signUp(userDTO);
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


}
