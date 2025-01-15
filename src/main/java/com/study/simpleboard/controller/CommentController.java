package com.study.simpleboard.controller;

import com.study.simpleboard.dto.CommentCreateRequestDTO;
import com.study.simpleboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<String> createComment(@PathVariable Long postId,
        @RequestBody CommentCreateRequestDTO requestDTO) { //TODO 인증객체에서 user 받기
        commentService.createComment(postId, 1L, requestDTO);
        return ResponseEntity.ok("댓글이 생성되었습니다"); //TODO response 수정
    }

}
