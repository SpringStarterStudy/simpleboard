package com.study.simpleboard.controller;

import com.study.simpleboard.common.response.ApiResponse;
import com.study.simpleboard.dto.PostDto;
import com.study.simpleboard.service.PostService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    // 게시물 상세 조회
    @GetMapping("/posts/{postId}")
    public ApiResponse<PostDto.PostResponse> getPostById(@PathVariable @Positive Long postId) {
        PostDto.PostResponse response = postService.findPostById(postId);
        postService.incrementViewCountAsync(postId);

        return ApiResponse.success("게시물을 성공적으로 조회했습니다.", response);
    }

}