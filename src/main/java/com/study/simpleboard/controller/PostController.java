package com.study.simpleboard.controller;

import com.study.simpleboard.common.response.ApiResponse;
import com.study.simpleboard.dto.PostDto;
import com.study.simpleboard.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.study.simpleboard.dto.PostCreateReq;
import org.springframework.http.ResponseEntity;

@Validated
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
  
    // 게시물 상세 조회
    @GetMapping("/posts/{postId}")
    public ApiResponse<PostDto.PostResponse> getPostById(
            @PathVariable @Positive(message = "게시물 요청 형식이 올바르지 않습니다.") Long postId) {
        PostDto.PostResponse response = postService.findPostById(postId);
        postService.incrementViewCountAsync(postId);

        return ApiResponse.success("게시물을 성공적으로 조회했습니다.", response);
    }
  
    // 게시물 삭제
    @DeleteMapping("/posts/{postId}")
    public ApiResponse<Void> deletePost(
            @PathVariable @Positive(message = "게시물 요청 형식이 올바르지 않습니다.") Long postId
    ) {
        // TODO: 사용자 검증
        postService.deletePost(postId, 1L);

        return ApiResponse.success("게시물을 성공적으로 삭제했습니다.");
    }

    // 게시물 작성
    @PostMapping("/api/posts")
    public ApiResponse<Void> savePost(@Valid @RequestBody PostCreateReq postCreateReq) {
        postService.savePost(postCreateReq);
        return ApiResponse.success("게시글이 저장되었습니다.");
    }
}