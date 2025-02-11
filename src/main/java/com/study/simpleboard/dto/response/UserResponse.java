package com.study.simpleboard.dto.response;

import com.study.simpleboard.dto.LoginType;
import com.study.simpleboard.dto.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long userId;
    private String name;
    private String email;
    private String cellPhone;
    private LoginType loginType;

    // User로부터 UserResponse를 생성하는 정적 팩토리 메서드
    public static UserResponse from(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .cellPhone(user.getCellPhone())
                .loginType(user.getLoginType())
                .build();
    }
}
