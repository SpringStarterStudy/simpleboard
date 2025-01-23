package com.study.simpleboard.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequest {
    @NotNull(message = "이름은 필수 입력값입니다.")
    @Size(min = 2, max = 8, message = "이름은 2자 이상 9자 이하여야 합니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9_]*$", message = "이름은 한글, 영문, 숫자, 언더바(_)만 사용 가능합니다.")
    private String name;

    @NotNull(message = "이메일은 필수 입력값입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotNull(message = "비밀번호는 필수 입력값입니다.")
    @Size(min = 10, message = "비밀번호는 10자 이상이어야 합니다.")
    @Pattern.List({
            @Pattern(regexp = ".*[!@#$%^&*(),.?\":{}|<>].*", message = "비밀번호는 특수문자를 포함해야 합니다."),
            @Pattern(regexp = ".*[A-Z].*", message = "비밀번호는 대문자를 포함해야 됩니다."),
            @Pattern(regexp = ".*[a-z].*", message = "비밀번호는 소문자를 포함해야 합니다."),
            @Pattern(regexp = "^\\S*$", message = "비밀번호는 공백을 포함할 수 없습니다.")
    })
    private String password;

    @NotNull(message = "휴대폰 번호는 필수 입력값입니다.")
    @Pattern(regexp = "^010\\d{8}$", message = "올바른 휴대폰 번호 형식이 아닙니다.")
    private String cellPhone;
}
