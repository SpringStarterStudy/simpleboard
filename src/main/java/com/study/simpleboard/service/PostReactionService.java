package com.study.simpleboard.service;

import com.study.simpleboard.common.exception.ErrorCode;
import com.study.simpleboard.domain.enums.ReactionType;
import com.study.simpleboard.dto.PostReactionReq;
import com.study.simpleboard.dto.PostReactionResp;
import com.study.simpleboard.domain.Reaction;
import com.study.simpleboard.mapper.PostMapper;
import com.study.simpleboard.repository.PostReactionRepository;
import com.study.simpleboard.service.exception.PostNotFoundException;
import com.study.simpleboard.service.exception.InvalidReactionException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostReactionService {
    private final PostReactionRepository postReactionRepository;
    private final PostMapper postMapper;

    // like, dislike 활성화 상태 조회
    // 조회된 데이터가 존재하지 않을 경우 false로 처리 후 반환
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated() and authentication.principal.userId == #userId")
    public PostReactionResp getReactionResponse(Long postId, Long userId) {
        validatePostId(postId);
        List<Reaction> reactions = postReactionRepository.findAllReactions(postId, userId);
        PostReactionResp resp = PostReactionResp.createDefault();

        for (Reaction reaction : reactions) {
            if (reaction.getReactionType() == ReactionType.LIKE) {
                resp = resp.changeLike(reaction.getActive());
            } else if (reaction.getReactionType() == ReactionType.DISLIKE) {
                resp = resp.changeDislike(reaction.getActive());
            }
        }

        return resp;
    }

    // like 또는 dislike 활성화 상태 갱신
    // 테이블에 데이터가 존재할 경우 update,
    // 존재하지 않을 경우 save
    @Transactional
    @PreAuthorize("isAuthenticated() and authentication.principal.userId == #userId")
    public void saveReactionRequest(Long postId, Long userId, PostReactionReq postReactionReq) {
        validatePostId(postId);
        ReactionType reactionType = getReactionType(postReactionReq);
        Optional<Reaction> postReaction = findReaction(postId, userId, reactionType);
        postReaction.ifPresentOrElse(
                reaction -> updateReaction(postReactionReq, reaction),
                () -> saveReaction(postId, userId, postReactionReq)
        );
    }

    private void validatePostId(Long postId) {
        if (!postMapper.existsById(postId)) {
            throw new PostNotFoundException(ErrorCode.POST_NOT_FOUND);
        }
    }

    private Optional<Reaction> findReaction(Long postId, Long userId, ReactionType reactionType) {
        return postReactionRepository.findReaction(postId, userId, reactionType);
    }

    private void updateReaction(PostReactionReq postReactionReq, Reaction reaction) {
        postReactionRepository.updateActive(getChangedReaction(postReactionReq, reaction));
    }

    private void saveReaction(Long postId, Long userId, PostReactionReq postReactionReq) {
        postReactionRepository.save(Reaction.of(userId, postId, getReactionType(postReactionReq), postReactionReq));
    }

    // 입력된 값이 like인지 dislike인지 확인 후 알맞은 ReactionType 반환
    private static ReactionType getReactionType(PostReactionReq postReactionReq) {
        if (postReactionReq.isInvalid()) {
            throw new InvalidReactionException(ErrorCode.INVALID_REACTION);
        }
        return postReactionReq.hasLike() ? ReactionType.LIKE : ReactionType.DISLIKE;
    }

    // 조회된 객체에서 reaction 활성화 상태만 변경한 후 반환
    private static Reaction getChangedReaction(PostReactionReq postReactionReq, Reaction reaction) {
        return reaction.changeActive(getReactionType(postReactionReq), postReactionReq.getActive());
    }
}
