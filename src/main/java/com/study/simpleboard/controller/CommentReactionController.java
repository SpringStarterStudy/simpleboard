package com.study.simpleboard.controller;

import com.study.simpleboard.common.response.ApiResponse;
import com.study.simpleboard.domain.enums.ReactionType;
import com.study.simpleboard.dto.CommentReactionRequestDTO;
import com.study.simpleboard.dto.CustomUserDetails;
import com.study.simpleboard.service.CommentReactionService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class CommentReactionController {

    private final CommentReactionService commentReactionService;

    public CommentReactionController(CommentReactionService commentReactionService) {
        this.commentReactionService = commentReactionService;
    }

    // 댓글 반응 업데이트
    @PostMapping("comments/{commentId}/reaction")
    public ApiResponse<Void> handleReaction(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentReactionRequestDTO inputReactionRequestDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        commentReactionService.updateCommentReaction(
                userDetails.getUserId(),
                commentId,
                inputReactionRequestDTO);
        return ApiResponse.success("Reaction processed successfully!");
    }

    // 댓글 반응 여부 조회
    @GetMapping("comments/{commentId}/reaction")
    public ApiResponse<Map<ReactionType, Boolean>> getCommentReactionStatus(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Map<ReactionType, Boolean> reactionStatus = commentReactionService.findCommentReactionStatus(userDetails.getUserId(), commentId);
        return ApiResponse.success("Comment reaction status retrieved successfully!", reactionStatus);
    }
}