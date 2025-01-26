package com.study.simpleboard.domain;

import com.study.simpleboard.dto.PostDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Post {
    private final Long id;
    private final Long userId;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime deletedAt;
    private final Long viewCount;

    @Builder
    public Post(Long userId, String title, String content, LocalDateTime createdAt,
                LocalDateTime updatedAt, LocalDateTime deletedAt, Long viewCount) {
        this(null, userId, title, content, createdAt, updatedAt, deletedAt, viewCount);
    }

    public static Post update(PostDto.UpdateRequest updateRequest) {
        return Post.builder()
                .userId(updateRequest.getUserId())
                .title(updateRequest.getTitle())
                .content(updateRequest.getContent())
                .build();
    }

}