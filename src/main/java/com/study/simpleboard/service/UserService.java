package com.study.simpleboard.service;

import com.study.simpleboard.common.exception.CustomException;
import com.study.simpleboard.common.exception.ErrorCode;
import com.study.simpleboard.dto.UserDTO;
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
    public UserDTO signUp(UserDTO userDTO) {
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

            UserDTO savedUser = userMapper.findByEmail(userDTO.getEmail()); // 저장된 사용자 정보 조회 (비밀번호는 제외)
            return savedUser;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.SIGNUP_FAILED);
        }
    }

}

