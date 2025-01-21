package com.study.simpleboard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

public class PostDto {

    @Getter
    public static class UpdateRequest {
        @NotNull @Positive private Long userId; // TODO: 사용자 인증

        @NotNull(message = "제목을 입력해주세요.")
        @NotBlank(message = "글자가 포함되어야 합니다.")
        @Size(max = 30, message = "제목은 30자 이내로 입력해주세요.")
        private String title;

        @NotNull(message = "내용을 입력해주세요.")
        @NotBlank(message = "글자가 포함되어야 합니다.")
        private String content;
    }
}