package com.study.simpleboard.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentResponseDTO {
    private Long commentId;
    private Long userId;
    private Long postId;
    private Long parentCommentId;
    private String commentContent;
    private LocalDateTime createdAt;

}
