package com.study.simpleboard.validator;

import com.study.simpleboard.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailValidator {
    private final UserMapper userMapper;

    public void validateEmail (String email) {

        // 이메일 중복 체크
        if (userMapper.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
    }
}
