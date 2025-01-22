package com.study.simpleboard.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteUserRequest {
    // 탈퇴 시 확인을 위해 비밀번호 입력하기
    @NotNull(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}
