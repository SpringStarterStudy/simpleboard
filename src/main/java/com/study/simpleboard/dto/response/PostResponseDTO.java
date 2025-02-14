package com.study.simpleboard.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponseDTO {

    @Getter
    public static class PostList {
        private Long id;
        private Long userId;
        private String title;
        private LocalDateTime createdAt;
        private Long viewCount;
    }

    // 전체 게시물의 간략한 정보와 페이지네이션
    @Getter
    @Builder
    public static class PostsAndPageResponse<T> {
        private List<PostList> postList;
        private Integer currentPage;    // 현재 페이지
        private Integer currentSize;    // 현재 페이지에 있는 게시물 수
        private Integer postPerPage;    // 페이지당 게시물 수
        private long totalPostsCount;   // 전체 게시물 수
        private Integer totalPages;     // 전체 페이지의 수
        private Integer pageGroupSize;  // 페이지 그룹의 크기 (default:5)

        public static <T> PostsAndPageResponse<T> of(
                List<PostList> postList, int currentPage, int currentSize, int postPerPage,
                long totalPostsCount, int totalPages, int pageGroupSize
        ) {
            return PostsAndPageResponse.<T>builder()
                    .postList(postList)
                    .currentPage(currentPage)
                    .currentSize(currentSize)
                    .postPerPage(postPerPage)
                    .totalPostsCount(totalPostsCount)
                    .totalPages(totalPages)
                    .pageGroupSize(pageGroupSize)
                    .build();
        }

        public static <T> PostsAndPageResponse<T> empty(Pageable pageable, int pageGroupSize) {
            return PostsAndPageResponse.<T>builder()
                    .postList(List.of()) // 빈 리스트 반환
                    .currentPage(pageable.getPageNumber() + 1)
                    .currentSize(0)
                    .postPerPage(pageable.getPageSize())
                    .totalPostsCount(0L)
                    .totalPages(0)
                    .pageGroupSize(pageGroupSize)
                    .build();
        }
    }

    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PostDetail {
        private Long id;
        private Long userId;
        private String title;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long viewCount;
    }

}