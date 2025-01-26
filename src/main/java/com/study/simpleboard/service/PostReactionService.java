package com.study.simpleboard.service;

import com.study.simpleboard.common.exception.ErrorCode;
import com.study.simpleboard.domain.enums.ReactionType;
import com.study.simpleboard.dto.PostReactionReq;
import com.study.simpleboard.dto.PostReactionResp;
import com.study.simpleboard.domain.Reaction;
import com.study.simpleboard.repository.PostReactionRepository;
import com.study.simpleboard.service.exception.InvalidReactionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostReactionService {
    private final PostReactionRepository postReactionRepository;

    // like, dislike 활성화 상태 조회
    // 조회된 데이터가 존재하지 않을 경우 false로 처리 후 반환
    @Transactional(readOnly = true)
    public PostReactionResp getReactionResponse(Long postId, Long userId) {
        // TODO: 유효한 postId인지 post 테이블에서 조회한 후, 존재하지 않는 경우 예외 처리 추가
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
    public void saveReactionRequest(Long postId, PostReactionReq postReactionReq) {
        // TODO: 유효한 postId인지 post 테이블에서 조회한 후, 존재하지 않는 경우 예외 처리 추가
        ReactionType reactionType = getReactionType(postReactionReq);
        Optional<Reaction> postReaction = findReaction(postId, postReactionReq, reactionType);
        postReaction.ifPresentOrElse(
                reaction -> updateReaction(postReactionReq, reaction),
                () -> saveReaction(postId, postReactionReq)
        );
    }

    private Optional<Reaction> findReaction(Long postId, PostReactionReq postReactionReq, ReactionType reactionType) {
        return postReactionRepository.findReaction(postId, postReactionReq.getUserId(), reactionType);
    }

    private void updateReaction(PostReactionReq postReactionReq, Reaction reaction) {
        postReactionRepository.updateActive(getChangedReaction(postReactionReq, reaction));
    }

    private void saveReaction(Long postId, PostReactionReq postReactionReq) {
        postReactionRepository.save(Reaction.of(postId, getReactionType(postReactionReq), postReactionReq));
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
