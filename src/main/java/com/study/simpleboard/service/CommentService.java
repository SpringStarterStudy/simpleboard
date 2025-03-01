package com.study.simpleboard.service;

import com.study.simpleboard.common.exception.CustomException;
import com.study.simpleboard.common.exception.ErrorCode;
import com.study.simpleboard.dto.CommentCreateDTO;
import com.study.simpleboard.dto.request.CommentRequestDTO;
import com.study.simpleboard.dto.response.CommentResponseDTO;
import com.study.simpleboard.dto.CommentRequestDTO;
import com.study.simpleboard.dto.CommentResponseDTO;
import com.study.simpleboard.dto.ReplyCreateDTO;
import com.study.simpleboard.mapper.CommentMapper;
import com.study.simpleboard.mapper.PostMapper;
import java.util.List;

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

        if (!postMapper.existsById(postId)) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        }

        commentMapper.insertComment(new CommentCreateDTO(userId, postId,
            requestDTO.getCommentContent()));

    }

    @Transactional
    public void createReply(Long userId, CommentRequestDTO requestDTO, Long parentId) {
        Long postId = commentMapper.findPostIdByCommentId(parentId);

        if (postId == null){
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        commentMapper.insertReply(new ReplyCreateDTO(userId, postId,
                requestDTO.getCommentContent(), parentId));

    }

    public List<CommentResponseDTO> getCommentList(Long postId) {

        if (!postMapper.existsById(postId)) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        }

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
