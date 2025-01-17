package com.study.simpleboard.dto;

import java.time.LocalDateTime;

public class CommentCreateDTO {
    private final Long userId;
    private final Long postId;
    private final String commentContent;
    private final LocalDateTime createdAt;

    public CommentCreateDTO(Long userId, Long postId, String commentContent) {
        this.userId = userId;
        this.postId = postId;
        this.commentContent = commentContent;
        this.createdAt = LocalDateTime.now();
    }
}
