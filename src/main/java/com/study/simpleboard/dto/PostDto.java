package com.study.simpleboard.dto;

import lombok.*;

import java.util.List;

public class PostDto {

    // 게시물의 정보
    @Getter
    public static class Info {
        private Long id;
        private Long userId;
        private String title;
        private String content;
        private String createdAt;
        private String updatedAt;
        private String deletedAt;
        private Long viewCount;
    }

    // 전체 게시물의 정보와 페이지네이션
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostsAndPageResponse<T> {
        private List<Info> content;
        private Integer currentPage;    // 현재 페이지
        private Integer currentSize;    // 현재 페이지에 있는 게시물 수
        private Integer postPerPage;    // 페이지당 게시물 수
        private long totalPostsCount;   // 전체 게시물 수
        private Integer totalPages;     // 전체 페이지의 수
        private Integer pageGroupSize;  // 페이지 그룹의 크기 (default:5)
    }

}