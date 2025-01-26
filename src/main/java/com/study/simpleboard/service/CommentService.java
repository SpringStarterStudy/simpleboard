package com.study.simpleboard.service;

import com.study.simpleboard.common.exception.CustomException;
import com.study.simpleboard.common.exception.ErrorCode;
import com.study.simpleboard.dto.CommentCreateDTO;
import com.study.simpleboard.dto.CommentCreateRequestDTO;
import com.study.simpleboard.dto.CommentResponseDTO;
import com.study.simpleboard.mapper.CommentMapper;
import java.util.List;
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

    public List<CommentResponseDTO> getCommentList(Long postId) {
        //TODO postId 유효성 검사
        return commentMapper.selectCommentList(postId);
    }

    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        if(commentMapper.checkCommentId(commentId) == 0) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        if(commentMapper.checkUser(userId, commentId) == 0) {
            throw new CustomException(ErrorCode.NO_COMMENT_AUTHORITY);
        }

        commentMapper.deleteComment(commentId);
    }

}
