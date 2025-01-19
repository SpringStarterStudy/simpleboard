package com.study.simpleboard.mapper;

import com.study.simpleboard.dto.CommentReactionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentReactionMapper {

    // 새 반응 삽입
    void insertCommentReaction(CommentReactionDTO commentReactionDTO);

    // 반응 타입 및 활성화 상태 업데이트
    void updateCommentReaction(CommentReactionDTO reaction);

    // 활성화 상태만 업데이트
    void updateReactionStatus(@Param("reactionId") Long reactionId, @Param("isActive") boolean isActive);

    // 특정 사용자와 댓글 ID에 해당하는 반응 조회
    CommentReactionDTO findByUserIdAndCommentId(@Param("userId") Long userId, @Param("targetId") Long targetId);
}
