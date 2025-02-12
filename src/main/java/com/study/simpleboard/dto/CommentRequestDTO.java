package com.study.simpleboard.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentRequestDTO {
    @Size(max = 200, message = "댓글은 200자 이내로만 작성 가능합니다.")
    private String commentContent;
}
