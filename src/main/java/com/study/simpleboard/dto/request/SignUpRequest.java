package com.study.simpleboard.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    @NotNull(message = "이름은 필수 입력값입니다.")
    private String name;

    @NotNull(message = "이메일은 필수 입력값입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotNull(message = "비밀번호는 필수 입력값입니다.")
    private String password;

    @NotNull(message = "휴대폰 번호는 필수 입력값입니다.")
    private String cellPhone;
}
