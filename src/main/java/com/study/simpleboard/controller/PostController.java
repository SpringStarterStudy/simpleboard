package com.study.simpleboard.controller;

import com.study.simpleboard.common.response.ApiResponse;
import com.study.simpleboard.dto.CustomUserDetails;
import com.study.simpleboard.dto.request.PostRequestDTO;
import com.study.simpleboard.dto.response.PostResponseDTO;
import com.study.simpleboard.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public ApiResponse<PostResponseDTO.PostsAndPageResponse<PostResponseDTO.PostList>> getAllPosts(
            @Valid @ModelAttribute PostRequestDTO.SearchRequest searchRequest
            ) {

        PostResponseDTO.PostsAndPageResponse<PostResponseDTO.PostList> response =
                postService.findAllPosts(searchRequest.toPageable(),
                        searchRequest.getSearchKeyword(), searchRequest.getSearchUser());

        return ApiResponse.success("게시물 목록을 성공적으로 조회했습니다.", response);
    }

    @PostMapping("/posts")
    public ApiResponse<Void> savePost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid PostRequestDTO.CreateAndUpdate request
    ) {

        postService.savePost(request, userDetails.getUserId());
        return ApiResponse.success("게시물이 저장되었습니다.");
    }

    @GetMapping("/posts/{postId}")
    public ApiResponse<PostResponseDTO.PostDetail> getPostById(
            @Positive(message = "게시물 요청 형식이 올바르지 않습니다.") @PathVariable Long postId
    ) {
        PostResponseDTO.PostDetail response = postService.findPostById(postId);
        return ApiResponse.success("게시물을 성공적으로 조회했습니다.", response);
    }

    @PutMapping("/posts/{postId}")
    public ApiResponse<Void> updatePost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Positive(message = "게시물 요청 형식이 올바르지 않습니다.") @PathVariable Long postId,
            @Valid @RequestBody PostRequestDTO.CreateAndUpdate request
    ) {
        postService.updatePost(postId, userDetails.getUserId(), request);
        return ApiResponse.success("게시물이 성공적으로 수정되었습니다.");
    }

    @DeleteMapping("/posts/{postId}")
    public ApiResponse<Void> deletePost(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Positive(message = "게시물 요청 형식이 올바르지 않습니다.") @PathVariable Long postId
    ) {
        postService.deletePost(postId, userDetails.getUserId());
        return ApiResponse.success("게시물을 성공적으로 삭제했습니다.");
    }

}