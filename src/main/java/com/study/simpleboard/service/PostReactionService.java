package com.study.simpleboard.service;

import com.study.simpleboard.common.exception.ErrorCode;
import com.study.simpleboard.domain.enums.ReactionType;
import com.study.simpleboard.dto.CustomUserDetails;
import com.study.simpleboard.dto.request.PostReactionRequest;
import com.study.simpleboard.dto.response.PostReactionResponse;
import com.study.simpleboard.domain.Reaction;
import com.study.simpleboard.mapper.PostMapper;
import com.study.simpleboard.repository.PostReactionRepository;
import com.study.simpleboard.service.exception.PostNotFoundException;
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
    // 조회된 데이터가 존재하지 않을 경우 false로 반환
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated() and authentication.principal.userId == #userDetails.userId")
    public PostReactionResponse getReactionResponse(Long postId, CustomUserDetails userDetails) {
        validatePostId(postId);
        List<Reaction> reactions = postReactionRepository.findAllReactions(postId, userDetails.getUserId());
        return PostReactionResponse.from(reactions);
    }

    // like 또는 dislike 활성화 상태 갱신
    // 테이블에 데이터가 존재할 경우 update,
    // 존재하지 않을 경우 save
    @Transactional
    @PreAuthorize("isAuthenticated() and authentication.principal.userId == #userDetails.userId")
    public void saveReactionRequest(Long postId, CustomUserDetails userDetails, PostReactionRequest postReactionRequest) {
        validatePostId(postId);
        Long userId = userDetails.getUserId();
        ReactionType reactionType = ReactionType.getReactionType(postReactionRequest);
        Optional<Reaction> postReaction = postReactionRepository.findReaction(postId, userId, reactionType);
        postReaction.ifPresentOrElse(
                reaction -> postReactionRepository.updateActive(changeActive(postReactionRequest, reaction)),
                () -> postReactionRepository.save(Reaction.of(userId, postId, postReactionRequest))
        );
    }

    // postId 검증
    private void validatePostId(Long postId) {
        if (!postMapper.existsById(postId)) {
            throw new PostNotFoundException(ErrorCode.POST_NOT_FOUND);
        }
    }

    // 조회된 객체에서 reaction 활성화 상태만 변경한 후 반환
    private static Reaction changeActive(PostReactionRequest postReactionRequest, Reaction reaction) {
        return reaction.changeActive(postReactionRequest.getActive());
    }
}
