package com.study.simpleboard.service;

import com.study.simpleboard.common.exception.CustomException;
import com.study.simpleboard.common.exception.ErrorCode;
import com.study.simpleboard.dto.LoginType;
import com.study.simpleboard.dto.User;
import com.study.simpleboard.dto.request.LoginRequest;
import com.study.simpleboard.dto.request.SignUpRequest;
import com.study.simpleboard.dto.request.UpdatePasswordRequest;
import com.study.simpleboard.dto.request.UpdateUserRequest;
import com.study.simpleboard.dto.response.UserResponse;
import com.study.simpleboard.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor // final 필드에 대한 생성자 자동 생성
@Transactional
public class UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화를 위한 인코더

    // 회원 가입
    public UserResponse signUp(SignUpRequest signUpRequest) {
        // 회원 가입 검증
        validateDuplicateUser(signUpRequest.getName(), signUpRequest.getEmail());

        User user = User.builder()
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .name(signUpRequest.getName())
                .cellPhone(signUpRequest.getCellPhone())
                .isEnabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .loginType(LoginType.LOCAL)
                .build();

        User savedUser = userMapper.findByEmail(user.getEmail());
        return UserResponse.from(savedUser);
    }

    // 로그인
    public UserResponse login(LoginRequest loginRequest) {
        User user = findUserByEmail(loginRequest.getEmail());
        validatePassword(loginRequest.getPassword(), user.getPassword());
        return UserResponse.from(user);
    }

    // 단일 정보 조회
    public UserResponse findById(Long userId) {
        User user = findUserById(userId);
        return UserResponse.from(user);
    }

    // 정보 수정
    public UserResponse updateUser(Long userId, UpdateUserRequest updateUserRequest) {
        User user = findUserById(userId);
        validateDuplicateName(updateUserRequest.getName(), userId);

        user.setName(updateUserRequest.getName());
        user.setCellPhone(updateUserRequest.getCellPhone());

        userMapper.updateUser(user);
        return UserResponse.from(userMapper.findById(userId));
    }

    // 비밀번호 수정
    public void updatePassword(Long userId, UpdatePasswordRequest updatePasswordRequest) {
        User user = findUserById(userId);

        validateCurrentPassword(updatePasswordRequest.getCurrentPassword(), user.getPassword());
        validateNewPassword(updatePasswordRequest.getNewPassword(), updatePasswordRequest.getConfirmPassword(), user.getPassword());

        String encodedPassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());
        userMapper.updatePassword(userId, encodedPassword);
    }

    // 회원 탈퇴
    public void deleteUser(Long userId, String password) {
        User user = findUserById(userId);
        validatePassword(password, user.getPassword());
        userMapper.deleteUser(userId);
    }



    // 검증 메서드들
    // 1. 회원가입 시 사용되는 검증들
    private void validateDuplicateUser(String name, String email) {
        // 이름 중복 검사
        if (userMapper.existsByName(name)) {
            throw new CustomException(ErrorCode.DUPLICATE_NAME);
        }
        // 이메일 중복 검사
        if (userMapper.existsByEmail(email)) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    // 2. 로그인 시 사용되는 검증
    private User findUserByEmail(String email) { // 이메일로 사용자 조회
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    // 3. 회원정보 조회, 수정, 삭제에서 사용되는 검증
    private User findUserById(Long userId) { // ID로 사용자 조회
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    // 4. 로그인, 회원탈퇴 시 사용되는 검증
    private void validatePassword(String rawPassword, String encodedPassword) {
        // 입력된 비밀번호와 암호화된 비밀번호 비교
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
    }

    // 5. 비밀번호 변경 시 사용되는 검증
    private void validateCurrentPassword(String currentPassword, String userPassword) {
        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(currentPassword, userPassword)) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
    }
    private void validateNewPassword(String newPassword, String confirmPassword, String currentPassword) {
        // 새 비밀번호와 현재 비밀번호가 일치하는지 확인
        if (!newPassword.equals(confirmPassword)) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }
        if (passwordEncoder.matches(newPassword, currentPassword)) {
            throw new CustomException(ErrorCode.SAME_AS_OLD_PASSWORD);
        }
    }

    // 6. 회원 정보 수정시 사용되는 검증
    private void validateDuplicateName(String name, Long userId) {
        // 변경하려는 이름이 이미 사용중인지 확인 (본인의 현재 이름은 중복 체크에서 제외)
        if (userMapper.existsByNameAndNotId(name, userId)) {
            throw new CustomException(ErrorCode.DUPLICATE_NAME);
        }
    }

}

