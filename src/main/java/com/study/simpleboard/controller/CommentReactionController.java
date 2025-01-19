package com.study.simpleboard.controller;

import com.study.simpleboard.common.ApiResponse;
import com.study.simpleboard.dto.CommentReactionDTO;
import com.study.simpleboard.service.CommentReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ApiResponse<CommentReactionDTO> getReaction(
            @PathVariable Long commentId,
            @RequestParam Long userId) {
        CommentReactionDTO reaction = commentReactionService.findReactionByUserAndComment(userId, commentId);

        return reaction != null
                ? ApiResponse.success("Reaction retrieved successfully!", reaction)
                : ApiResponse.success("No reaction found",null);
    }
}