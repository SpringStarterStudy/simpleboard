package com.study.simpleboard.controller;

import com.study.simpleboard.common.response.ApiResponse;
import com.study.simpleboard.dto.CustomUserDetails;
import com.study.simpleboard.dto.request.PostReactionRequest;
import com.study.simpleboard.dto.response.PostReactionResponse;
import com.study.simpleboard.service.PostReactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostReactionController {
    private final PostReactionService postReactionService;

    // like, dislike 활성화 여부 조회
    // 검증 실패 시 400 응답
    // 조회 성공 시 200 응답
    @GetMapping("/posts/{postId}/reaction")
    public ApiResponse<PostReactionResponse> getReaction(@Positive @PathVariable Long postId,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        PostReactionResponse reactionResponse = postReactionService.getReactionResponse(postId, userDetails);
        return ApiResponse.success(reactionResponse);
    }

    // like 또는 dislike 활성화 상태에 대한 요청을 받아서 저장한 후 204 응답
    // 검증 실패 시 400 응답
    // 요청받은 데이터에 like 또는 dislike가 존재하지 않거나,
    // like와 dislike가 둘 다 존재할 경우 400 응답
    @PostMapping("/posts/{postId}/reaction")
    public ResponseEntity<Void> saveReaction(@Positive @PathVariable Long postId,
                                             @Valid @RequestBody PostReactionRequest postReactionRequest,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        postReactionService.saveReactionRequest(postId, userDetails, postReactionRequest);
        return ResponseEntity.noContent().build();
    }
}
