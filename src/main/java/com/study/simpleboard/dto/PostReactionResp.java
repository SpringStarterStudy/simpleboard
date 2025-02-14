package com.study.simpleboard.dto;

import com.study.simpleboard.domain.Reaction;
import com.study.simpleboard.domain.enums.ReactionType;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class PostReactionResp {
    private final LikeDislikeStatus like;
    private final LikeDislikeStatus dislike;

    public static PostReactionResp from(List<Reaction> reactions) {
        PostReactionResp resp = createDefault();
        for(Reaction reaction : reactions) {
            resp = resp.changeStatus(reaction.getActive(), reaction.getReactionType());
        }
        return resp;
    }

    public static PostReactionResp createDefault() {
        return new PostReactionResp(LikeDislikeStatus.createFalse(), LikeDislikeStatus.createFalse());
    }

    public PostReactionResp changeStatus(boolean active, ReactionType reactionType) {
        return reactionType == ReactionType.LIKE ? changeLike(active) : changeDislike(active);
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
