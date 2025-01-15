package com.study.simpleboard.service;

import com.study.simpleboard.dto.CommentCreateDTO;
import com.study.simpleboard.dto.CommentCreateRequestDTO;
import com.study.simpleboard.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;

    public void createComment(Long postId, Long userId, CommentCreateRequestDTO requestDTO) {
        //TODO postId 유효성 검사
        commentMapper.insertComment(new CommentCreateDTO(userId, postId,
            requestDTO.getCommentContent()));

    }
}
