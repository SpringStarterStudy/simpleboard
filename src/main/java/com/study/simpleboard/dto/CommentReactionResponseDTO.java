package com.study.simpleboard.dto;

import com.study.simpleboard.domain.ReactionType;
import com.study.simpleboard.domain.TargetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentReactionResponseDTO {
    private Long reactionId;
    private Long userId;
    private Long targetId;
    private TargetType targetType;
    private ReactionType reactionType;
    private Boolean isActive;
}

