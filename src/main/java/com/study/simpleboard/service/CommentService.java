package com.study.simpleboard.service;

import com.study.simpleboard.dto.CommentCreateDTO;
import com.study.simpleboard.dto.CommentCreateRequestDTO;
import com.study.simpleboard.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;

    @Transactional
    public void createComment(Long postId, Long userId, CommentCreateRequestDTO requestDTO) {
        //TODO postId 유효성 검사
        commentMapper.insertComment(new CommentCreateDTO(userId, postId,
            requestDTO.getCommentContent()));

    }
}
