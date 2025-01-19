package com.study.simpleboard.controller;

import com.study.simpleboard.dto.post.PostReactionReq;
import com.study.simpleboard.dto.post.PostReactionResp;
import com.study.simpleboard.service.PostReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostReactionController {
    private final PostReactionService postReactionService;

    // like, dislike 활성화 여부 조회
    // 조회 데이터 없을 시 404 응답
    // 조회 성공 시 200 응답
    @GetMapping("/api/posts/{postId}/reaction")
    public ResponseEntity<PostReactionResp> getReaction(@PathVariable Long postId, @RequestParam Long userId) {
        // TODO: 로그인 인증 구현 후 userId 검증 추가
        //  파라미터 검증 추가
        PostReactionResp reactionResponse = postReactionService.getReactionResponse(postId, userId);
        return ResponseEntity.ok(reactionResponse);
    }

    // like 또는 dislike 활성화 상태에 대한 요청을 받아서 저장한 후 204 응답
    // 요청받은 데이터에 like 또는 dislike가 존재하지 않을 경우 400 응답
    @PostMapping("/api/posts/{postId}/reaction")
    public ResponseEntity<Void> saveReaction(@PathVariable Long postId, @RequestBody PostReactionReq postReactionReq) {
        // TODO: 로그인 인증 구현 후 userId 검증 추가
        //  파라미터 검증 추가
        postReactionService.saveReactionRequest(postId, postReactionReq);
        return ResponseEntity.noContent().build();
    }
}
