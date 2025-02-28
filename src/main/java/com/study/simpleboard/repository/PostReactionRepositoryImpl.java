package com.study.simpleboard.repository;

import com.study.simpleboard.domain.enums.ReactionType;
import com.study.simpleboard.domain.enums.TargetType;
import com.study.simpleboard.domain.Reaction;
import com.study.simpleboard.mapper.ReactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostReactionRepositoryImpl implements PostReactionRepository {
    private final ReactionMapper reactionMapper;

    // 게시글 id, 사용자 id를 가지고,
    // 게시글 like, dislike 상태 조회
    @Override
    public List<Reaction> findAllReactions(Long postId, Long userId) {
        return reactionMapper.findAllReactions(postId, userId, TargetType.POST);
    }

    // 게시글 id, 사용자 id, reactionType을 가지고,
    // like 또는 dislike 데이터 단건 조회
    @Override
    public Optional<Reaction> findReaction(Long postId, Long userId, ReactionType reactionType) {
        return reactionMapper.findReaction(postId, userId, TargetType.POST, reactionType);
    }

    @Override
    public void save(Reaction reaction) {
        reactionMapper.save(reaction);
    }

    // like 또는 dislike의 활성화 여부 갱신
    @Override
    public void updateActive(Reaction reaction) {
        reactionMapper.updateActive(reaction);
    }
}
