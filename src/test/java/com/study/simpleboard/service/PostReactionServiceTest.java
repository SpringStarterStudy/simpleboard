package com.study.simpleboard.service;

import com.study.simpleboard.common.exception.ErrorCode;
import com.study.simpleboard.domain.Reaction;
import com.study.simpleboard.dto.PostReactionReq;
import com.study.simpleboard.dto.PostReactionResp;
import com.study.simpleboard.domain.enums.ReactionType;
import com.study.simpleboard.domain.enums.TargetType;
import com.study.simpleboard.repository.PostReactionRepository;
import com.study.simpleboard.service.exception.InvalidReactionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostReactionServiceTest {
    private final static Long USER_ID = 1L;
    private final static Long POST_ID = 15L;
    private final static boolean LIKE_STATUS = true;
    private final static boolean DISLIKE_STATUS = true;

    @Mock
    private PostReactionRepository postReactionRepository;

    @InjectMocks
    private PostReactionService postReactionService;

    @DisplayName("like, dislike 활성화 상태 조회 - like와 dislike 둘 다 존재할 경우")
    @Test
    void getReactionResponse_withLikeAndDislike_returnsList() {
        // 조건: "like, dislike 상태가 둘 다 존재할 경우"
        // 기대 결과: "like=조회 데이터, dislike=조회 데이터 반환"

        // Given: Mock 데이터 정의
        List<Reaction> mockReactions = List.of(
                getReaction(ReactionType.LIKE, LIKE_STATUS), getReaction(ReactionType.DISLIKE, DISLIKE_STATUS));
        when(postReactionRepository.findAllReactions(POST_ID, USER_ID)).thenReturn(mockReactions);

        // When: Service 메서드 호출
        PostReactionResp reactionResponse = postReactionService.getReactionResponse(POST_ID, USER_ID);

        // Then: 결과 검증
        PostReactionResp resp = PostReactionResp.createDefault()
                .changeLike(LIKE_STATUS)
                .changeDislike(DISLIKE_STATUS);
        assertThat(reactionResponse).isEqualTo(resp);

        // Then: Mapper 호출 검증
        verify(postReactionRepository).findAllReactions(POST_ID, USER_ID);
    }

    @DisplayName("like, dislike 활성화 상태 조회 - like 상태만 존재할 경우")
    @Test
    void getReactionResponse_withLike_returnsList() {
        // 조건: "like 상태만 존재할 경우"
        // 기대 결과: "like=조회 데이터, dislike=false 반환"

        // Given: Mock 데이터 정의
        List<Reaction> mockReactions = List.of(getReaction(ReactionType.LIKE, LIKE_STATUS));
        when(postReactionRepository.findAllReactions(POST_ID, USER_ID)).thenReturn(mockReactions);

        // When: Service 메서드 호출
        PostReactionResp reactionResponse = postReactionService.getReactionResponse(POST_ID, USER_ID);

        // Then: 결과 검증
        PostReactionResp resp = PostReactionResp.createDefault().changeLike(LIKE_STATUS);
        assertThat(reactionResponse).isEqualTo(resp);

        // Then: Mapper 호출 검증
        verify(postReactionRepository).findAllReactions(POST_ID, USER_ID);
    }

    @DisplayName("like, dislike 활성화 상태 조회 - 데이터가 존재하지 않은 경우")
    @Test
    void getReactionResponse_whenEmpty_returnsList() {
        // 조건: "데이터가 존재하지 않은 경우"
        // 기대 결과: "like=false, dislike=false 반환"

        // Given: Mock 데이터 정의
        List<Reaction> mockReactions = List.of();
        when(postReactionRepository.findAllReactions(POST_ID, USER_ID)).thenReturn(mockReactions);

        // When: Service 메서드 호출
        PostReactionResp reactionResponse = postReactionService.getReactionResponse(POST_ID, USER_ID);

        // Then: 결과 검증
        PostReactionResp resp = PostReactionResp.createDefault();
        assertThat(reactionResponse).isEqualTo(resp);

        // Then: Mapper 호출 검증
        verify(postReactionRepository).findAllReactions(POST_ID, USER_ID);
    }

    @DisplayName("like 또는 dislike 활성화 상태 갱신 - 데이터가 존재할 경우 updateActive 호출")
    @Test
    void saveReactionRequest_whenDataExists_shouldCallUpdateActive() {
        // 조건: "like 데이터가 존재하는 경우"
        // 기대 결과: "데이터 갱신"

        // Given: Mock 데이터 정의
        PostReactionReq mockReq = new PostReactionReq(USER_ID, LIKE_STATUS, null);
        Optional<Reaction> mockReaction = Optional.of(getReaction(ReactionType.LIKE, LIKE_STATUS));
        when(postReactionRepository.findReaction(POST_ID, mockReq.getUserId(), ReactionType.LIKE)).thenReturn(mockReaction);

        // When: Service 메서드 호출
        postReactionService.saveReactionRequest(POST_ID, mockReq);

        // Then: Mapper 호출 검증
        verify(postReactionRepository).findReaction(POST_ID, mockReq.getUserId(), ReactionType.LIKE);
        verify(postReactionRepository).updateActive(any(Reaction.class));
    }

    @DisplayName("like 또는 dislike 활성화 상태 갱신 - 데이터가 존재하지 않을 경우 save 호출")
    @Test
    void saveReactionRequest_whenDataExists_shouldCallSave() {
        // 조건: "데이터가 존재하는 않은 경우"
        // 기대 결과: "데이터 저장"

        // Given: Mock 데이터 정의
        PostReactionReq mockReq = new PostReactionReq(USER_ID, LIKE_STATUS, null);
        Optional<Reaction> mockReaction = Optional.empty();
        when(postReactionRepository.findReaction(POST_ID, mockReq.getUserId(), ReactionType.LIKE)).thenReturn(mockReaction);

        // When: Service 메서드 호출
        postReactionService.saveReactionRequest(POST_ID, mockReq);

        // Then: Mapper 호출 검증
        verify(postReactionRepository).findReaction(POST_ID, mockReq.getUserId(), ReactionType.LIKE);
        verify(postReactionRepository).save(any(Reaction.class));
    }

    @DisplayName("like 또는 dislike 활성화 상태 갱신 - like와 dislike가 둘 다 null일 경우")
    @Test
    void saveReactionRequest_whenLikeAndDislikeIsNull_shouldThrowException() {
        // 조건: "like와 dislike가 둘 다 null일 경우"
        // 기대 결과: "throw InvalidReactionException"

        // Given: Mock 데이터 정의
        PostReactionReq mockReq = new PostReactionReq(USER_ID, null, null);

        // When: Service 메서드 호출
        assertThatThrownBy(() -> postReactionService.saveReactionRequest(POST_ID, mockReq))
                .isInstanceOf(InvalidReactionException.class)
                .hasMessage(ErrorCode.INVALID_REACTION.getMessage())
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_REACTION);

        verify(postReactionRepository,times(0)).findReaction(anyLong(), anyLong(), any(ReactionType.class));
    }

    @DisplayName("like 또는 dislike 활성화 상태 갱신 - like와 dislike의 데이터가 둘 다 존재할 경우")
    @Test
    void saveReactionRequest_whenLikeAndDislikeExist_shouldThrowException() {
        // 조건: "like와 dislike의 데이터가 둘 다 존재할 경우"
        // 기대 결과: "throw InvalidReactionException"

        // Given: Mock 데이터 정의
        PostReactionReq mockReq = new PostReactionReq(USER_ID, LIKE_STATUS, DISLIKE_STATUS);

        // When: Service 메서드 호출
        assertThatThrownBy(() -> postReactionService.saveReactionRequest(POST_ID, mockReq))
                .isInstanceOf(InvalidReactionException.class)
                .hasMessage(ErrorCode.INVALID_REACTION.getMessage())
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_REACTION);

        verify(postReactionRepository,times(0)).findReaction(anyLong(), anyLong(), any(ReactionType.class));
    }

    // TODO: userId 검증 테스트 코드 추가
    //  postId 검증 테스트 추가

    private static Reaction getReaction(ReactionType reactionType, boolean active) {
        Reaction reaction = Reaction.builder()
                .userId(USER_ID)
                .targetId(POST_ID)
                .targetType(TargetType.POST)
                .reactionType(reactionType)
                .active(active)
                .build();
        ReflectionTestUtils.setField(reaction, "id", 1L);
        return reaction;
    }
}