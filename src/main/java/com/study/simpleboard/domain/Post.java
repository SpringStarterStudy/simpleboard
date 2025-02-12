package com.study.simpleboard.domain;

import com.study.simpleboard.dto.request.PostRequestDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
public class Post {
    private final Long id;
    private final Long userId;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime deletedAt;
    private final Long viewCount;

    public static Post from(PostRequestDTO.CreateAndUpdate createAndUpdate, Long userId) {
        return Post.builder()
                .userId(userId)
                .title(createAndUpdate.getTitle())
                .content(createAndUpdate.getContent())
                .build();
    }

}