package com.study.simpleboard.controller;

import com.study.simpleboard.common.response.ApiResponse;
import com.study.simpleboard.dto.PostDto;
import com.study.simpleboard.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    // 게시글 목록 전체 보기
    @GetMapping("/posts")
    public ApiResponse<PostDto.PostsAndPageResponse<PostDto.ListInfo>> getAllPosts(
            @Valid @ModelAttribute PostDto.SearchRequest searchRequest
            ) {

        PostDto.PostsAndPageResponse<PostDto.ListInfo> response =
                postService.findAllPosts(searchRequest.toPageable(),
                        searchRequest.getSearchKeyword(), searchRequest.getSearchUser());

        return ApiResponse.success("게시물 목록을 성공적으로 조회했습니다.", response);
    }
}
