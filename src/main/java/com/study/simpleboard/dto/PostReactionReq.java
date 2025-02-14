package com.study.simpleboard.dto;

import com.study.simpleboard.common.exception.ErrorCode;
import com.study.simpleboard.service.exception.InvalidReactionException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class PostReactionReq {
    private final Boolean like;
    private final Boolean dislike;

    public boolean hasLike() {
        return like != null;
    }

    public boolean hasDislike() {
        return dislike != null;
    }

    public void validate() {
        if (isInvalid()) {
            throw new InvalidReactionException(ErrorCode.INVALID_REACTION);
        }
    }

    private boolean isInvalid() {
        return !hasLike() && !hasDislike() || hasLike() && hasDislike();
    }

    public boolean getActive() {
        return hasLike() ? getLike() : getDislike();
    }
}
