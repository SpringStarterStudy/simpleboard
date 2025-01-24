package com.study.simpleboard.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class PostReactionReq {
    // userId 검증 임시 추가
    @NotNull
    @Positive
    private final Long userId;
    private final Boolean like;
    private final Boolean dislike;

    public boolean hasLike() {
        return like != null;
    }

    public boolean hasDislike() {
        return dislike != null;
    }

    public boolean isInvalid() {
        return !hasLike() && !hasDislike() || hasLike() && hasDislike();
    }

    public boolean getActive() {
        return hasLike() ? getLike() : getDislike();
    }
}
