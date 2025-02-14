package com.study.simpleboard.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PostRequestDTO {

    @Getter
    @AllArgsConstructor
    public static class CreateAndUpdate {
        @NotNull(message = "제목을 입력해주세요.")
        @NotBlank(message = "제목은 공백으로 만들 수 없습니다. 글자를 포함시켜주세요.")
        @Size(max = 30, message = "제목은 30자 이내로 입력해주세요.")
        private String title;

        @NotNull(message = "내용을 입력해주세요.")
        @NotBlank(message = "내용은 공백으로 만들 수 없습니다. 글자를 포함시켜주세요.")
        @Size(max = 10000, message = "내용은 10,000자 이내로 입력해주세요.")
        private String content;
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