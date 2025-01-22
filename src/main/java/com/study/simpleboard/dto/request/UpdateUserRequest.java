package com.study.simpleboard.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    @NotNull(message = "이름은 필수 입력값입니다.")
    @Size(min = 2, max = 8, message = "이름은 2자 이상 9자 이하여야 합니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9_]*$", message = "이름은 한글, 영문, 숫자, 언더바(_)만 사용 가능합니다.")
    private String name;

    @NotNull(message = "휴대폰 번호는 필수 입력값입니다.")
    @Pattern(regexp = "^010\\d{8}$", message = "올바른 휴대폰 번호 형식이 아닙니다.")
    private String cellPhone;
}
