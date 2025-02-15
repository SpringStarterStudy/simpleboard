package com.study.simpleboard.dto.response;

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
public class PostReactionResponse {
    private final LikeDislikeStatus like;
    private final LikeDislikeStatus dislike;

    public static PostReactionResponse from(List<Reaction> reactions) {
        PostReactionResponse response = createDefault();
        for(Reaction reaction : reactions) {
            response = response.changeStatus(reaction.getActive(), reaction.getReactionType());
        }
        return response;
    }

    public static PostReactionResponse createDefault() {
        return new PostReactionResponse(LikeDislikeStatus.createFalse(), LikeDislikeStatus.createFalse());
    }

    public PostReactionResponse changeStatus(boolean active, ReactionType reactionType) {
        return reactionType == ReactionType.LIKE ? changeLike(active) : changeDislike(active);
    }

    public PostReactionResponse changeLike(boolean active) {
        return new PostReactionResponse(LikeDislikeStatus.of(active), this.dislike);
    }

    public PostReactionResponse changeDislike(boolean active) {
        return new PostReactionResponse(this.like, LikeDislikeStatus.of(active));
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
