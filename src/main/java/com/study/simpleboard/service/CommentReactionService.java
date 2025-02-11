package com.study.simpleboard.service;

import com.study.simpleboard.common.exception.CustomException;
import com.study.simpleboard.common.exception.ErrorCode;
import com.study.simpleboard.domain.enums.ReactionType;
import com.study.simpleboard.dto.CommentReactionRequestDTO;
import com.study.simpleboard.dto.CommentReactionResponseDTO;
import com.study.simpleboard.mapper.CommentMapper;
import com.study.simpleboard.mapper.CommentReactionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class CommentReactionService {

    private final CommentReactionMapper commentReactionMapper;
    private final CommentMapper commentMapper;

    public CommentReactionService(CommentReactionMapper commentReactionMapper, CommentMapper commentMapper) {
        this.commentReactionMapper = commentReactionMapper;
        this.commentMapper = commentMapper;
    }

    // 특정 유저가 특정 댓글에 반응을 하였는지 여부
    @Transactional(readOnly = true)
    public Map<ReactionType, Boolean> findCommentReactionStatus(Long userId, Long commentId) {
        // 특정 사용자의 반응 조회
        List<CommentReactionResponseDTO> reactions = commentReactionMapper.findAllByUserIdAndCommentId(userId, commentId);

        // 기본값 설정 (LIKE, DISLIKE 모두 비활성화 상태)
        Map<ReactionType, Boolean> reactionStatus = new HashMap<>();
        reactionStatus.put(ReactionType.LIKE, false);
        reactionStatus.put(ReactionType.DISLIKE, false);

        // 조회 결과에 따라 활성화 상태 업데이트
        reactions.forEach(reaction -> reactionStatus.put(reaction.getReactionType(), reaction.getIsActive()));

        return reactionStatus;
    }

    // 유저 반응 업데이트
    @Transactional
    public void updateCommentReaction(Long userId, Long commentId, CommentReactionRequestDTO inputReactionRequestDTO) {
        // 댓글이 현재 유효한지 검증
        if (!commentMapper.existsByCommentId(commentId)) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);  // C_001: 존재하지 않는 댓글
        }

        if (inputReactionRequestDTO == null || inputReactionRequestDTO.getReactionType() == null) {
            throw new CustomException(ErrorCode.INVALID_REACTION);
        }

        // 반응 타입까지 포함하여 조회
        CommentReactionResponseDTO existingReaction = commentReactionMapper.findByUserIdCommentIdAndReactionType(
                userId,
                commentId,
                inputReactionRequestDTO.getReactionType()
        );
        if (existingReaction == null) {
            // 새 반응 추가
            insertCommentReaction(userId, commentId, inputReactionRequestDTO);
        } else {
            // 기존 반응의 isActive 상태 반전
             toggleReactionStatus(existingReaction);
        }
    }

    private void toggleReactionStatus(CommentReactionResponseDTO existingReaction) {
        // 기존 활성화 상태를 반전
        boolean newIsActive = !existingReaction.getIsActive();

        // Mapper에 업데이트 요청
        commentReactionMapper.updateReactionStatus(existingReaction.getReactionId(), newIsActive);
    }


    private void insertCommentReaction(Long userId, Long commentId, CommentReactionRequestDTO inputReactionDTO) {
        commentReactionMapper.insertCommentReaction(
                userId,
                commentId,
                inputReactionDTO.getReactionType()
        );
    }
}
