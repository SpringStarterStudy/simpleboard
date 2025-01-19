package com.study.simpleboard.service;

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
    public void signUp(UserDTO userDTO) {
        // 중복 체크
        if (userMapper.existsByName(userDTO.getName())) {
            throw new IllegalArgumentException("이미 존재하는 이름입니다.");
        }

        if (userMapper.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encodedPassword);

        try {
            // DB에 회원 정보 저장
            userMapper.insertUser(userDTO);
        } catch (Exception e) {
            throw new RuntimeException("회원 가입 처리 중 오류가 발생했습니다.", e);
        }
    }

}

