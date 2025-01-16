package com.study.simpleboard.dto.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostReactionResp {
    private final LikeDislikeStatus like;
    private final LikeDislikeStatus dislike;

    public static PostReactionResp createDefault() {
        return new PostReactionResp(new LikeDislikeStatus(false), new LikeDislikeStatus(false));
    }

    public PostReactionResp changeLike(boolean active) {
        return new PostReactionResp(new LikeDislikeStatus(active), this.dislike);
    }

    public PostReactionResp changeDislike(boolean active) {
        return new PostReactionResp(this.like, new LikeDislikeStatus(active));
    }

    @Getter
    @RequiredArgsConstructor
    static class LikeDislikeStatus {
        private final boolean active;
    }
}
