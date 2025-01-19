package com.study.simpleboard.service;

import com.study.simpleboard.dto.UserDTO;
import com.study.simpleboard.mapper.UserMapper;
import com.study.simpleboard.validator.CellphoneValidator;
import com.study.simpleboard.validator.EmailValidator;
import com.study.simpleboard.validator.NameValidator;
import com.study.simpleboard.validator.PasswordValidator;
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
    private final NameValidator nameValidator;
    private final PasswordValidator passwordValidator;
    private final EmailValidator emailValidator;
    private final CellphoneValidator cellphoneValidator;

    // 회원 가입
    public void signUp(UserDTO userDTO) {

        // 유효성 검사
        nameValidator.validateName(userDTO.getName());
        passwordValidator.validatePassword(userDTO.getPassword());
        emailValidator.validateEmail(userDTO.getEmail());
        cellphoneValidator.validateCellphone(userDTO.getCellPhone());

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

