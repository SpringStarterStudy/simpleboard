package com.study.simpleboard.repository;

import com.study.simpleboard.enums.ReactionType;
import com.study.simpleboard.enums.TargetType;
import com.study.simpleboard.dto.post.PostReactionReq;
import com.study.simpleboard.domain.Reaction;
import com.study.simpleboard.mapper.ReactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostReactionRepository {
    private final ReactionMapper reactionMapper;

    // 게시글 id, 사용자 id를 가지고,
    // 게시글 like, dislike 상태 조회
    public List<Reaction> findAllReactions(Long postId, Long userId) {
        return reactionMapper.findAllReactions(postId, userId, TargetType.POST);
    }

    // 게시글 id, 사용자 id, reactionType을 가지고,
    // like 또는 dislike 데이터 단건 조회
    public Optional<Reaction> findReaction(Long postId, PostReactionReq postReactionReq, ReactionType reactionType) {
        return reactionMapper.findReaction(postId, postReactionReq.getUserId(), TargetType.POST, reactionType);
    }

    public Reaction save(Reaction reaction) {
        reactionMapper.save(reaction);
        return reaction;
    }

    // like 또는 dislike의 활성화 여부 갱신
    public Reaction updateActive(Reaction reaction) {
        reactionMapper.updateActive(reaction);
        return reaction;
    }
}