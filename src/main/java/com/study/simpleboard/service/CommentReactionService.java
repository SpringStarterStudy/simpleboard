package com.study.simpleboard.service;

import com.study.simpleboard.domain.ReactionType;
import com.study.simpleboard.domain.TargetType;
import com.study.simpleboard.dto.CommentReactionDTO;
import com.study.simpleboard.mapper.CommentReactionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CommentReactionService {

    private final CommentReactionMapper commentReactionMapper;

    public CommentReactionService(CommentReactionMapper commentReactionMapper) {
        this.commentReactionMapper = commentReactionMapper;
    }

    // 특정 유저가 특정 댓글에 반응을 하였는지 여부
    @Transactional(readOnly = true)
    public CommentReactionDTO findReactionByUserAndComment(Long userId, Long commentId) {
        return commentReactionMapper.findByUserIdAndCommentId(userId, commentId);
    }

    // 유저 반응 업데이트
    @Transactional
    public void updateCommentReaction(Long userId, Long commentId, CommentReactionDTO inputReactionDTO) {
        CommentReactionDTO existingReaction = findReactionByUserAndComment(userId, commentId);

        if (existingReaction == null) {
            // 새 반응 추가
            insertCommentReaction(userId, commentId, inputReactionDTO);
        } else if (existingReaction.reactionType().equals(inputReactionDTO.reactionType())) {
            // 동일한 타입 → isActive 상태 반전
            toggleReactionStatus(existingReaction);
        } else {
            // 다른 타입 → 타입 변경
            updateReactionType(existingReaction, inputReactionDTO.reactionType());
        }
    }

    private void toggleReactionStatus(CommentReactionDTO existingReaction) {
        // 기존 활성화 상태를 반전
        boolean newIsActive = !existingReaction.isActive();

        // Mapper에 업데이트 요청
        commentReactionMapper.updateReactionStatus(existingReaction.reactionId(), newIsActive);
    }

    private void updateReactionType(CommentReactionDTO existingReaction, ReactionType newReactionType) {
        // 새 타입으로 변경하고 활성화 상태 설정
        CommentReactionDTO updatedReaction = new CommentReactionDTO(
                existingReaction.reactionId(),
                existingReaction.userId(),
                existingReaction.targetId(),
                existingReaction.targetType(),
                newReactionType,
                true // 항상 활성화 상태로 설정
        );

        commentReactionMapper.updateCommentReaction(updatedReaction);
    }

    private void insertCommentReaction(Long userId, Long commentId, CommentReactionDTO inputReactionDTO) {
        // 새 반응 추가 시 항상 활성화 상태로 설정
        CommentReactionDTO newReaction = new CommentReactionDTO(
                null,
                userId,
                commentId,
                TargetType.COMMENT,
                inputReactionDTO.reactionType(),
                true // 기본 활성화 상태
        );

        commentReactionMapper.insertCommentReaction(newReaction);
    }
}
