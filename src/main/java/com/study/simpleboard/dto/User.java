package com.study.simpleboard.dto;


import lombok.*;

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

}
