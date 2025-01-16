package com.study.simpleboard.dto;

import com.study.simpleboard.domain.ReactionType;
import com.study.simpleboard.domain.TargetType;

public record CommentReactionDTO(
        Long reactionId,
        Long userId,
        Long targetId,
        TargetType targetType,
        ReactionType reactionType,
        Boolean isActive
) {}
