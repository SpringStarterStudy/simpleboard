package com.study.simpleboard.dto.post;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class PostReactionResp {
    private final LikeDislikeStatus like;
    private final LikeDislikeStatus dislike;

    public static PostReactionResp createDefault() {
        return new PostReactionResp(LikeDislikeStatus.createFalse(), LikeDislikeStatus.createFalse());
    }

    public PostReactionResp changeLike(boolean active) {
        return new PostReactionResp(LikeDislikeStatus.of(active), this.dislike);
    }

    public PostReactionResp changeDislike(boolean active) {
        return new PostReactionResp(this.like, LikeDislikeStatus.of(active));
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @EqualsAndHashCode
    static class LikeDislikeStatus {
        private final boolean active;

        private static LikeDislikeStatus of(boolean active) {
            return new LikeDislikeStatus(active);
        }

        private static LikeDislikeStatus createTrue() {
            return new LikeDislikeStatus(true);
        }

        private static LikeDislikeStatus createFalse() {
            return new LikeDislikeStatus(false);
        }
    }
}
