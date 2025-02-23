package com.study.simpleboard.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class PostRequestDTO {

    @Getter
    @AllArgsConstructor
    public static class CreateAndUpdate {
        @NotBlank(message = "제목을 입력해주세요.")
        @Size(max = 30, message = "제목은 30자 이내로 입력해주세요.")
        private String title;

        @NotBlank(message = "내용을 입력해주세요.")
        @Size(max = 10000, message = "내용은 10,000자 이내로 입력해주세요.")
        private String content;
    }
}