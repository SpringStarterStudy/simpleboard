package com.study.simpleboard.repository;

import com.study.simpleboard.domain.Reaction;
import com.study.simpleboard.domain.enums.ReactionType;

import java.util.List;
import java.util.Optional;

public interface PostReactionRepository {
    List<Reaction> findAllReactions(Long postId, Long userId);

    Optional<Reaction> findReaction(Long postId, Long userId, ReactionType reactionType);

    void save(Reaction reaction);

    void updateActive(Reaction reaction);
}
