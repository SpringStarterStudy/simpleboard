package com.study.simpleboard.service;

import com.study.simpleboard.common.exception.CustomException;
import com.study.simpleboard.common.exception.ErrorCode;
import com.study.simpleboard.dto.User;
import com.study.simpleboard.dto.request.LoginRequest;
import com.study.simpleboard.dto.request.UpdatePasswordRequest;
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

    // 비밀번호 수정
    public void updatePassword(Long userId, UpdatePasswordRequest request) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // 새 비밀번호와 확인 비밀번호가 일치하지 않으면
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        // 새 비밀번호가 현재 비밀번호와 같다면
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.SAME_AS_OLD_PASSWORD);
        }

        // 새 비밀번호 암호화 후 유저 정보에 업데이트
        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        userMapper.updatePassword(userId, encodedPassword);
    }

    // 회원 탈퇴
    public void deleteUser(Long userId, String password) {
        // 사용자 확인
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        try {
            userMapper.deleteUser(userId);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.DELETE_USER_FAILED);
        }
    }

}

