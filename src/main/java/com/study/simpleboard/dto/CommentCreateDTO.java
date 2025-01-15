package com.study.simpleboard.dto;

import java.time.LocalDateTime;

public class CommentCreateDTO {
    private Long userId;
    private Long postId;
    private String commentContent;
    private LocalDateTime createdAt;

    public CommentCreateDTO(Long userId, Long postId, String commentContent) {
        this.userId = userId;
        this.postId = postId;
        this.commentContent = commentContent;
        this.createdAt = LocalDateTime.now();
    }
}
