package com.study.simpleboard.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class PasswordValidator { // 비밀번호 유효성 검사
    private static final String SPECIAL_CHARS = ".*[!@#$%^&*((),.?\":{}|<>`~].*";
    private static final int MIN_LENGTH = 10;

    public void validatePassword (String password) {
        List<String> errorMessages = new ArrayList<>();

        // 길이 체크
        if (password.length() < MIN_LENGTH) {
            errorMessages.add("비밀번호는 " + MIN_LENGTH + "자 이상이어야 합니다!");
        }

        // 특수 문자 포함 여부
        if (!password.matches(SPECIAL_CHARS)) {
            errorMessages.add("비밀번호는 특수문자를 포함해야 합니다.");
        }

        // 대문자 포함 여부
        if (!password.matches(".*[A-Z].*")) {
            errorMessages.add("비밀번호는 대문자를 포함해야 됩니다.");
        }

        // 소문자 포함 여부
        if (!password.matches(".*[a-z].*")) {
            errorMessages.add("비밀번호는 소문자를 포함해야 합니다.");
        }

        // 공백 포함 여부
        if (password.contains(" ")) {
            errorMessages.add("비밀번호는 공백을 포함할 수 없습니다.");
        }

        if (!errorMessages.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n", errorMessages));
        }

    }

}
