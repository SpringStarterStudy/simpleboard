package com.study.simpleboard.controller;

import com.study.simpleboard.common.response.ApiResponse;
import com.study.simpleboard.dto.PostCreateReq;
import com.study.simpleboard.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/api/posts")
    public ResponseEntity<ApiResponse<Void>> savePost(@RequestBody PostCreateReq postCreateReq) {
        postService.savePost(postCreateReq);
        return ResponseEntity.ok(ApiResponse.success("게시글이 저장되었습니다."));
    }
}
