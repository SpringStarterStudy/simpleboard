package com.study.simpleboard.controller;

import com.study.simpleboard.common.response.ApiResponse;
import com.study.simpleboard.dto.PostDto;
import com.study.simpleboard.service.PostService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    // 게시글 목록 전체 보기
    @GetMapping("/posts")
    public ApiResponse<PostDto.PostsAndPageResponse<PostDto.Info>> postList(
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "페이지는 1부터 시작합니다.") Integer page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "사이즈는 최소 1 이상이어야 합니다.") Integer size,
            @RequestParam(required = false, defaultValue = "") String searchKeyword,
            @RequestParam(required = false, defaultValue = "") String searchUser
            ) {

        Pageable pageable = PageRequest.of(page - 1, size);
        PostDto.PostsAndPageResponse<PostDto.Info> response = postService.findAllPost(pageable, searchKeyword, searchUser);

        return ApiResponse.success("게시물 목록을 성공적으로 조회했습니다.", response);
    }
}
