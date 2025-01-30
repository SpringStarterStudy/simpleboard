package com.study.simpleboard.dto;

import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;

public class PostDto {

    @Getter
    public static class ListInfo {
        private Long id;
        private Long userId;
        private String title;
        private String createdAt;
        private Long viewCount;
    }

    // 전체 게시물의 간략한 정보와 페이지네이션
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostsAndPageResponse<T> {
        private List<ListInfo> postList;
        private Integer currentPage;    // 현재 페이지
        private Integer currentSize;    // 현재 페이지에 있는 게시물 수
        private Integer postPerPage;    // 페이지당 게시물 수
        private long totalPostsCount;   // 전체 게시물 수
        private Integer totalPages;     // 전체 페이지의 수
        private Integer pageGroupSize;  // 페이지 그룹의 크기 (default:5)
    }

    // 검색 조건과 페이징 정보
    @Getter
    @Setter
    @NoArgsConstructor
    public static class SearchRequest {
        @Min(value = 1, message = "페이지는 1부터 시작합니다.")
        private Integer page = 1;

        @Min(value = 1, message = "사이즈는 최소 1 이상이어야 합니다.")
        private Integer size = 10;

        private String searchKeyword = "";
        private String searchUser = "";

        public Pageable toPageable() {
            return PageRequest.of(page - 1, size);
        }
    }

}