package com.study.simpleboard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostCreateReq {
    // 나중에 인증 구현 후 수정 예정이라 임시로 만듦
    @NotNull
    @Positive
    private final Long userId;

    @NotNull(message = "제목이 존재하지 않습니다.")
    @NotBlank(message = "제목을 입력해주세요.")
    private final String title;

    @NotNull(message = "내용이 존재하지 않습니다.")
    @NotBlank(message = "내용을 입력해주세요.")
    private final String content;
}
