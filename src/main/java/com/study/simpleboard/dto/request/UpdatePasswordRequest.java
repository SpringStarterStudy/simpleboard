package com.study.simpleboard.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordRequest {
    @NotNull(message = "현재 비밀번호는 필수 입력값입니다.")
    private String currentPassword;

    @NotNull(message = "새 비밀번호는 필수 입력값입니다.")
    @Size(min = 10, message = "비밀번호는 10자 이상이어야 합니다.")
    @Pattern.List({
            @Pattern(regexp = ".*[!@#$%^&*(),.?\":{}|<>].*", message = "비밀번호는 특수문자를 포함해야 합니다."),
            @Pattern(regexp = ".*[A-Z].*", message = "비밀번호는 대문자를 포함해야 됩니다."),
            @Pattern(regexp = ".*[a-z].*", message = "비밀번호는 소문자를 포함해야 합니다."),
            @Pattern(regexp = "^\\S*$", message = "비밀번호는 공백을 포함할 수 없습니다.")
    })
    private String newPassword;

    @NotNull(message = "비밀번호 확인은 필수 입력값입니다.")
    private String confirmPassword;
}
