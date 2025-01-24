package com.study.simpleboard.service;

import com.study.simpleboard.domain.enums.ReactionType;
import com.study.simpleboard.dto.CommentReactionRequestDTO;
import com.study.simpleboard.dto.CommentReactionResponseDTO;
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
        // TODO 댓글이 현재 유효한지 검증
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
        // 새 반응 추가 시 항상 활성화 상태로 설정
        CommentReactionRequestDTO newReaction = new CommentReactionRequestDTO(
                userId,
                commentId,
                inputReactionDTO.getReactionType()
        );
        commentReactionMapper.insertCommentReaction(newReaction);
    }
}
