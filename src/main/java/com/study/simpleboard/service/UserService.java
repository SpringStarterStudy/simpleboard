package com.study.simpleboard.service;

import com.study.simpleboard.common.exception.CustomException;
import com.study.simpleboard.common.exception.ErrorCode;
import com.study.simpleboard.dto.User;
import com.study.simpleboard.dto.request.LoginRequest;
import com.study.simpleboard.dto.request.UpdateUserRequest;
import com.study.simpleboard.dto.response.LoginResponse;
import com.study.simpleboard.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor // final 필드에 대한 생성자 자동 생성
@Transactional
public class UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화를 위한 인코더

    // 회원 가입
    public User signUp(User userDTO) {
        // 중복 체크
        if (userMapper.existsByName(userDTO.getName())) {
            throw new CustomException(ErrorCode.DUPLICATE_NAME);
        }
        if (userMapper.existsByEmail(userDTO.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encodedPassword);

        try {
            userMapper.insertUser(userDTO); // DB에 회원 정보 저장

            User savedUser = userMapper.findByEmail(userDTO.getEmail()); // 저장된 사용자 정보 조회 (비밀번호는 제외)
            return savedUser;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.SIGNUP_FAILED);
        }
    }

    // 회원 로그인
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userMapper.findByEmail(loginRequest.getEmail()); // 이메일로 사용자 조회
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) { // 비밀번호 검증
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // 비밀번호는 포함하지 않고 반환
        return LoginResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    // 단일 회원 조회
    public User findById(Long userId) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        user.setPassword(null); // 보안을 위해 비밀번호는 포함 안하기
        return user;
    }

    // 정보 수정
    public User updateUser(Long userId, UpdateUserRequest request) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        // 이름 중복 체크 (이미 있는 이름인지)
        if (userMapper.existsByNameAndNotId(request.getName(), userId)) {
            throw new CustomException(ErrorCode.DUPLICATE_NAME);
        }

        user.setName(request.getName());
        user.setCellPhone(request.getCellPhone());

        userMapper.updateUser(user);
        return userMapper.findById(userId);
    }

}

