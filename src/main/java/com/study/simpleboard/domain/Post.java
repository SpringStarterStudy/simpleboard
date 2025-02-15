package com.study.simpleboard.domain;

import com.study.simpleboard.dto.PostDto;
import lombok.*;
import com.study.simpleboard.dto.request.PostCreateRequest;
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

    public static Post from(PostCreateRequest postCreateRequest) {
        return Post.builder()
                .userId(postCreateRequest.getUserId())
                .title(postCreateRequest.getTitle())
                .content(postCreateRequest.getContent())
                .createdAt(LocalDateTime.now())
                .viewCount(0L)
                .build();
    }
  
    public static Post fromUpdateRequest(PostDto.UpdateRequest updateRequest) {
        return Post.builder()
                .userId(updateRequest.getUserId())
                .title(updateRequest.getTitle())
                .content(updateRequest.getContent())
                .build();
    }
  
}
