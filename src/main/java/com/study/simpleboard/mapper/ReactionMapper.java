package com.study.simpleboard.mapper;

import com.study.simpleboard.domain.enums.ReactionType;
import com.study.simpleboard.domain.enums.TargetType;
import com.study.simpleboard.domain.Reaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ReactionMapper {
    List<Reaction> findAllReactions(@Param("targetId") Long postId,
                                    @Param("userId") Long userId,
                                    @Param("targetType") TargetType targetType);

    Optional<Reaction> findReaction(@Param("targetId") Long postId,
                                   @Param("userId") Long userId,
                                   @Param("targetType") TargetType targetType,
                                   @Param("reactionType") ReactionType reactionType);

    void save(@Param("reaction") Reaction reaction);

    void updateActive(@Param("reaction") Reaction reaction);
}
