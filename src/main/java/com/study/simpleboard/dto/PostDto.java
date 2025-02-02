package com.study.simpleboard.dto;

import lombok.*;

public class PostDto {

    // 게시물 상세 정보
    @Getter
    @Builder
    public static class PostResponse {
        private Long id;
        private Long userId;
        private String title;
        private String content;
        private String createdAt;
        private String updatedAt;
        private Long viewCount;
    }

}