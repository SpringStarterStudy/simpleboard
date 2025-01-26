package com.study.simpleboard.domain;

import com.study.simpleboard.domain.enums.ReactionType;
import com.study.simpleboard.domain.enums.TargetType;
import com.study.simpleboard.dto.PostReactionReq;
import lombok.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Reaction {
    private final Long id;
    private final Long userId;
    private final Long targetId;
    private final TargetType targetType;
    private final ReactionType reactionType;
    private final Boolean active;

    @Builder
    public Reaction(Long userId, Long targetId, TargetType targetType, ReactionType reactionType, Boolean active) {
        this(null, userId, targetId, targetType, reactionType, active);
    }

    public Reaction changeActive(ReactionType reactionType, Boolean active) {
        return Reaction.builder()
                .userId(userId)
                .targetId(targetId)
                .targetType(targetType)
                .reactionType(reactionType)
                .active(active)
                .build();
    }

    public static Reaction of(Long postId, ReactionType reactionType, PostReactionReq postReactionReq) {
        return Reaction.builder()
                .userId(postReactionReq.getUserId())
                .targetId(postId)
                .targetType(TargetType.POST)
                .reactionType(reactionType)
                .active(postReactionReq.getActive())
                .build();
    }
}