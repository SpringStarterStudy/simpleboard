package com.study.simpleboard.controller;

import com.study.simpleboard.common.response.ApiResponse;
import com.study.simpleboard.dto.CustomUserDetails;
import com.study.simpleboard.dto.PostDto;
import com.study.simpleboard.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.study.simpleboard.dto.request.PostCreateRequest;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    // 게시물 목록 전체 보기
    @GetMapping("/posts")
    public ApiResponse<PostDto.PostsAndPageResponse<PostDto.ListInfo>> getAllPosts(
            @Valid @ModelAttribute PostDto.SearchRequest searchRequest
            ) {

        PostDto.PostsAndPageResponse<PostDto.ListInfo> response =
                postService.findAllPosts(searchRequest.toPageable(),
                        searchRequest.getSearchKeyword(), searchRequest.getSearchUser());

        return ApiResponse.success("게시물 목록을 성공적으로 조회했습니다.", response);
    }
  
    // 게시물 작성
    @PostMapping("/posts")
    public ApiResponse<Void> savePost(@Valid @RequestBody PostCreateRequest postCreateRequest,
                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        postService.savePost(postCreateRequest, userDetails);
        return ApiResponse.success("게시물이 저장되었습니다.");
    }
  
    // 게시물 상세 조회
    @GetMapping("/posts/{postId}")
    public ApiResponse<PostDto.PostResponse> getPostById(
            @PathVariable @Positive(message = "게시물 요청 형식이 올바르지 않습니다.") Long postId) {
        PostDto.PostResponse response = postService.findPostById(postId);
        postService.incrementViewCountAsync(postId);

        return ApiResponse.success("게시물을 성공적으로 조회했습니다.", response);
    }
  
    // 게시물 수정
    @PutMapping("/posts/{postId}")
    public ApiResponse<Void> updatePost(
            @PathVariable @Positive(message = "게시물 요청 형식이 올바르지 않습니다.") Long postId,
            @Valid @RequestBody PostDto.UpdateRequest request
    ) {
        postService.updatePost(postId, request);

        return ApiResponse.success("게시물이 수정되었습니다.");
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

}