package com.study.simpleboard.controller;

import com.study.simpleboard.common.response.ApiResponse;
import com.study.simpleboard.dto.PostDto;
import com.study.simpleboard.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    // 게시물 수정
    @PutMapping("/posts/{postId}")
    public ApiResponse<Void> updatePost(
            @PathVariable @Positive(message = "게시물 번호는 양수여야 합니다.") Long postId,
            @Valid @RequestBody PostDto.UpdateRequest request
    ) {
        log.info("updatePost 컨트롤러 실행");
        postService.updatePost(postId, request);

        return ApiResponse.success("게시글이 수정되었습니다.");
    }

}