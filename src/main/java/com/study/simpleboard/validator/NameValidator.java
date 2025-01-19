package com.study.simpleboard.validator;

import com.study.simpleboard.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NameValidator {
    private final UserMapper userMapper;

    public void validateName (String name) {

        // 길이 체크
        if (name.length() < 2 || name.length() > 8) {
            throw new IllegalArgumentException("이름은 2자 이상 9자 이하여야 합니다.");
        }

        // 특수 문자
        if (!name.matches("^[가-힣a-zA-Z0-9_]*$")) {
            throw new IllegalArgumentException("이름은 한글, 영문, 숫자, 언더바(_)만 사용 가능합니다.");
        }

        // 중복 체크
        if (userMapper.existsByName(name)) {
            throw new IllegalArgumentException("이미 존재하는 이름입니다.");
        }
    }

}
