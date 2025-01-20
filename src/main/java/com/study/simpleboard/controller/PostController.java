package com.study.simpleboard.controller;

import com.study.simpleboard.common.response.ApiResponse;
import com.study.simpleboard.dto.PostCreateReq;
import com.study.simpleboard.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/api/posts")
    public ApiResponse<Void> savePost(@Valid @RequestBody PostCreateReq postCreateReq) {
        postService.savePost(postCreateReq);
        return ApiResponse.success("게시글이 저장되었습니다.");
    }
}
