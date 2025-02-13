package com.study.simpleboard.service;

import com.study.simpleboard.common.exception.CustomException;
import com.study.simpleboard.common.exception.ErrorCode;
import com.study.simpleboard.dto.CommentCreateDTO;
import com.study.simpleboard.dto.CommentRequestDTO;
import com.study.simpleboard.dto.CommentResponseDTO;
import com.study.simpleboard.dto.ReplyCreateDTO;
import com.study.simpleboard.mapper.CommentMapper;
import java.util.List;

import com.study.simpleboard.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;
    private final PostMapper postMapper;

    @Transactional
    public void createComment(Long postId, Long userId, CommentRequestDTO requestDTO) {
        //TODO postId 유효성 검사
        commentMapper.insertComment(new CommentCreateDTO(userId, postId,
            requestDTO.getCommentContent()));

    }

    @Transactional
    public void createReply(Long postId, Long userId, CommentRequestDTO requestDTO, Long parentId) {
        if (!postMapper.existsById(postId)) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        }

        if (!commentMapper.existsByCommentId(parentId)){
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        commentMapper.insertReply(new ReplyCreateDTO(userId, postId,
                requestDTO.getCommentContent(), parentId));

    }

    public List<CommentResponseDTO> getCommentList(Long postId) {
        //TODO postId 유효성 검사
        return commentMapper.selectCommentList(postId);
    }

    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        if(!commentMapper.checkCommentId(commentId)) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        if(!commentMapper.checkUser(userId, commentId)) {
            throw new CustomException(ErrorCode.NO_COMMENT_AUTHORITY);
        }

        commentMapper.deleteComment(commentId);
    }

    @Transactional
    public void updateComment(Long userId, Long commentId, CommentRequestDTO requestDTO) {

        if(!commentMapper.checkCommentId(commentId)) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        if(!commentMapper.checkUser(userId, commentId)) {
            throw new CustomException(ErrorCode.NO_COMMENT_AUTHORITY);
        }

        commentMapper.updateComment(commentId, requestDTO.getCommentContent());
    }

}
