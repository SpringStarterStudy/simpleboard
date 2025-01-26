package com.study.simpleboard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

public class PostDto {

    @Getter
    public static class UpdateRequest {
        @NotNull(message = "유저의 아이디를 입력해주세요.")
        @Positive(message = "유저 번호는 양수여야 합니다.")
        private Long userId; // 임시

        @NotNull(message = "제목을 입력해주세요.")
        @NotBlank(message = "글자가 포함되어야 합니다.")
        @Size(max = 30, message = "제목은 30자 이내로 입력해주세요.")
        private String title;

        @NotNull(message = "내용을 입력해주세요.")
        @NotBlank(message = "글자가 포함되어야 합니다.")
        private String content;
    }
}