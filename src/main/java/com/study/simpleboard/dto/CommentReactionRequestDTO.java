package com.study.simpleboard.dto;

import com.study.simpleboard.domain.enums.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentReactionRequestDTO {
    private Long userId;
    private Long targetId;
    private ReactionType reactionType;
}
