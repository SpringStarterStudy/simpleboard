package com.study.simpleboard.domain.enums;

import com.study.simpleboard.dto.request.PostReactionRequest;

public enum ReactionType {
    LIKE,
    DISLIKE;

    // 입력된 값이 like인지 dislike인지 확인 후 알맞은 ReactionType 반환
    public static ReactionType getReactionType(PostReactionRequest postReactionRequest) {
        postReactionRequest.validate();
        return postReactionRequest.hasLike() ? ReactionType.LIKE : ReactionType.DISLIKE;
    }
}
