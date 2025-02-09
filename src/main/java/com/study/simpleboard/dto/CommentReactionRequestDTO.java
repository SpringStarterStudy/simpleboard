package com.study.simpleboard.dto;

import com.study.simpleboard.domain.enums.ReactionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentReactionRequestDTO {
    private Long userId;
    @NotNull
    private Long targetId;
    @NotNull
    private ReactionType reactionType;
}
