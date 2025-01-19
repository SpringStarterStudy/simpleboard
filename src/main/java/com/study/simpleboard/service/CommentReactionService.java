package com.study.simpleboard.service;

import com.study.simpleboard.domain.ReactionType;
import com.study.simpleboard.domain.TargetType;
import com.study.simpleboard.dto.CommentReactionDTO;
import com.study.simpleboard.mapper.CommentReactionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class CommentReactionService {

    private final CommentReactionMapper commentReactionMapper;

    public CommentReactionService(CommentReactionMapper commentReactionMapper) {
        this.commentReactionMapper = commentReactionMapper;
    }

    // 특정 유저가 특정 댓글에 반응을 하였는지 여부
    @Transactional(readOnly = true)
    public Map<ReactionType, Boolean> findCommentReactionStatus(Long userId, Long commentId) {
        // 특정 사용자의 반응 조회
        List<CommentReactionDTO> reactions = commentReactionMapper.findAllByUserIdAndCommentId(userId, commentId);

        // 기본값 설정 (LIKE, DISLIKE 모두 비활성화 상태)
        Map<ReactionType, Boolean> reactionStatus = new HashMap<>();
        reactionStatus.put(ReactionType.LIKE, false);
        reactionStatus.put(ReactionType.DISLIKE, false);

        // 조회 결과에 따라 활성화 상태 업데이트
        reactions.forEach(reaction -> reactionStatus.put(reaction.reactionType(), reaction.isActive()));

        return reactionStatus;
    }

    // 유저 반응 업데이트
    @Transactional
    public void updateCommentReaction(Long userId, Long commentId, CommentReactionDTO inputReactionDTO) {
        // 반응 타입까지 포함하여 조회
        CommentReactionDTO existingReaction = commentReactionMapper.findByUserIdCommentIdAndReactionType(
                userId,
                commentId,
                inputReactionDTO.reactionType()
        );

        if (existingReaction == null) {
            // 새 반응 추가
            insertCommentReaction(userId, commentId, inputReactionDTO);
        } else {
            // 기존 반응의 isActive 상태 반전
            toggleReactionStatus(existingReaction);
        }
    }

    private void toggleReactionStatus(CommentReactionDTO existingReaction) {
        // 기존 활성화 상태를 반전
        boolean newIsActive = !existingReaction.isActive();

        // Mapper에 업데이트 요청
        commentReactionMapper.updateReactionStatus(existingReaction.reactionId(), newIsActive);
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
