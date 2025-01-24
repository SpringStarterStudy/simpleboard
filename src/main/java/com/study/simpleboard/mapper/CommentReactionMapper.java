package com.study.simpleboard.mapper;

import com.study.simpleboard.domain.enums.ReactionType;
import com.study.simpleboard.dto.CommentReactionRequestDTO;
import com.study.simpleboard.dto.CommentReactionResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentReactionMapper {

    // 새 반응 삽입
    void insertCommentReaction(CommentReactionRequestDTO commentReactionrequestDTO);

    // 활성화 상태만 업데이트
    void updateReactionStatus(@Param("reactionId") Long reactionId, @Param("isActive") boolean isActive);

    // 특정 사용자와 댓글 ID에 해당하는 반응 조회
    List<CommentReactionResponseDTO> findAllByUserIdAndCommentId(@Param("userId") Long userId, @Param("targetId") Long targetId);

    CommentReactionResponseDTO findByUserIdCommentIdAndReactionType(
            @Param("userId") Long userId,
            @Param("commentId") Long commentId,
            @Param("reactionType") ReactionType reactionType
    );
}
