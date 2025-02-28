package com.study.simpleboard.dto;

import java.time.LocalDateTime;

public class ReplyCreateDTO {
    private final Long userId;
    private final Long postId;
    private final String commentContent;
    private final LocalDateTime createdAt;
    private final Long parentId;

    public ReplyCreateDTO(Long userId, Long postId, String commentContent, Long parentId) {
        this.userId = userId;
        this.postId = postId;
        this.commentContent = commentContent;
        this.createdAt = LocalDateTime.now();
        this.parentId = parentId;
    }
}
