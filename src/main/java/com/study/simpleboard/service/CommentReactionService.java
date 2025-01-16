package com.study.simpleboard.service;

import com.study.simpleboard.domain.TargetType;
import com.study.simpleboard.dto.CommentReactionDTO;
import com.study.simpleboard.mapper.CommentReactionMapper;
import org.springframework.stereotype.Service;

@Service
public class CommentReactionService {

    private final CommentReactionMapper commentReactionMapper;

    public CommentReactionService(CommentReactionMapper commentReactionMapper) {
        this.commentReactionMapper = commentReactionMapper;
    }

    public void addCommentReaction(Long commentId, CommentReactionDTO commentReactionDTO){
        CommentReactionDTO reaction = new CommentReactionDTO(
                commentReactionDTO.reactionId(),
                commentReactionDTO.userId(),
                commentId,
                TargetType.COMMENT,
                commentReactionDTO.reactionType(),
                commentReactionDTO.isActive()
        );
        commentReactionMapper.insertCommentReaction(reaction);
    }

}
