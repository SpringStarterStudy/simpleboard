package com.study.simpleboard.validator;

import com.study.simpleboard.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CellphoneValidator {
    private final UserMapper userMapper;

    public void validateCellphone (String cellPhone) {

        // 형식 검증
        if (!cellPhone.matches("^\\d{11}$") || !cellPhone.startsWith("010")) {
            throw new IllegalArgumentException("올바른 휴대폰 번호 형식이 아닙니다.");
        }
    }
}
