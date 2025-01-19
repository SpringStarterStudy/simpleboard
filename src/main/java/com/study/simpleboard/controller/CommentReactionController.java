package com.study.simpleboard.controller;

import com.study.simpleboard.common.response.ApiResponse;
import com.study.simpleboard.domain.ReactionType;
import com.study.simpleboard.dto.CommentReactionDTO;
import com.study.simpleboard.service.CommentReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class CommentReactionController {

    @Autowired
    private CommentReactionService commentReactionService;

    // 댓글 반응 업데이트
    @PostMapping("comments/{commentId}/reaction")
    public ApiResponse<Void> handleReaction(
            @PathVariable Long commentId,
            @RequestBody CommentReactionDTO inputReactionDTO) {
        commentReactionService.updateCommentReaction(
                inputReactionDTO.userId(),
                commentId,
                inputReactionDTO);
        return ApiResponse.success("Reaction processed successfully!");
    }

    // 댓글 반응 여부 조회
    @GetMapping("comments/{commentId}/reaction")
    public ApiResponse<Map<ReactionType, Boolean>> getCommentReactionStatus(
            @PathVariable Long commentId,
            @RequestParam Long userId) {
        Map<ReactionType, Boolean> reactionStatus = commentReactionService.findCommentReactionStatus(userId, commentId);
        return ApiResponse.success("Comment reaction status retrieved successfully!", reactionStatus);
    }
}