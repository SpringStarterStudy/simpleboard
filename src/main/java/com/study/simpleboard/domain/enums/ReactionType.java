package com.study.simpleboard.domain.enums;

import com.study.simpleboard.dto.PostReactionReq;

public enum ReactionType {
    LIKE,
    DISLIKE;

    // 입력된 값이 like인지 dislike인지 확인 후 알맞은 ReactionType 반환
    public static ReactionType getReactionType(PostReactionReq postReactionReq) {
        postReactionReq.validate();
        return postReactionReq.hasLike() ? ReactionType.LIKE : ReactionType.DISLIKE;
    }
}
