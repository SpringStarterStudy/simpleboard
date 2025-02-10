package com.study.simpleboard.dto;


import com.study.simpleboard.common.exception.CustomException;
import com.study.simpleboard.common.exception.ErrorCode;
import lombok.*;
import org.springframework.security.authentication.DisabledException;

import java.time.LocalDateTime;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long userId;
    private String name;
    private String email;
    private String password;
    private String cellPhone;
    private boolean isEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LoginType loginType;

    // 정적 팩토리 메서드
    public static User createLocalUser(String email, String encodedPassword, String name, String cellPhone) {
        return User.builder()
                .email(email)
                .password(encodedPassword)
                .name(name)
                .cellPhone(cellPhone)
                .isEnabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .loginType(LoginType.LOCAL)
                .build();
    }

    // 카카오 유저 생성을 위한 정적 팩토리 메서드
    public static User createKakaoUser(KakaoUser kakaoUser) {
        return User.builder()
                .email(kakaoUser.getKakaoAccount().getEmail())
                .name(kakaoUser.getKakaoAccount().getProfile().getNickname())
                .isEnabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .loginType(LoginType.KAKAO)
                .build();
    }

    // 계정 활성화 여부
    public void validateEnabled() {
        if (!isEnabled) {
            throw new CustomException(ErrorCode.ACCOUNT_DISABLED);
        }
    }
}
