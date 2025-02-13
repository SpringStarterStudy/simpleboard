package com.study.simpleboard.controller;

import com.study.simpleboard.common.response.ApiResponse;
import com.study.simpleboard.dto.CommentRequestDTO;
import com.study.simpleboard.dto.CommentResponseDTO;
import com.study.simpleboard.dto.CustomUserDetails;
import com.study.simpleboard.service.CommentService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ApiResponse<Void> createComment(@PathVariable Long postId,
        @Valid @RequestBody CommentRequestDTO requestDTO) { //TODO 인증객체에서 user 받기
        commentService.createComment(postId, 1L, requestDTO);
        return ApiResponse.success("댓글이 생성되었습니다.");  //TODO response 수정
    }

    @PostMapping("/posts/{postId}/comments/{commentId}")
    public ApiResponse<Void> createComment(@PathVariable Long postId,
                                           @PathVariable Long commentId,
                                           @Valid @RequestBody CommentRequestDTO requestDTO,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        commentService.createReply(postId, userDetails.getUserId(), requestDTO, commentId);
        return ApiResponse.success(String.format("댓글 %d의 대댓글이 생성되었습니다.", postId));
    }

    @GetMapping("/posts/{postId}/comments")
    public ApiResponse<List<CommentResponseDTO>> getCommentList(@PathVariable Long postId) {
        return ApiResponse.success("댓글이 조회되었습니다.", commentService.getCommentList(postId));
    }

    @DeleteMapping("/comments/{commentId}")
    public ApiResponse<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(1L, commentId);
        return ApiResponse.success("댓글이 삭제되었습니다.");
    }

    @PutMapping("/comments/{commentId}")
    public ApiResponse<Void> updateComment(@PathVariable Long commentId,
        @Valid @RequestBody CommentRequestDTO requestDTO) {
        commentService.updateComment(2L, commentId, requestDTO);
        return ApiResponse.success("댓글이 수정되었습니다.");  //TODO response 수정
    }


}
