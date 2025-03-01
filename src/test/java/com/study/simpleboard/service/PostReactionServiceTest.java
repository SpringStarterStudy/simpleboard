package com.study.simpleboard.service;

import com.study.simpleboard.common.exception.ErrorCode;
import com.study.simpleboard.domain.Reaction;
import com.study.simpleboard.dto.CustomUserDetails;
import com.study.simpleboard.dto.request.PostReactionRequest;
import com.study.simpleboard.dto.response.PostReactionResponse;
import com.study.simpleboard.domain.enums.ReactionType;
import com.study.simpleboard.domain.enums.TargetType;
import com.study.simpleboard.dto.User;
import com.study.simpleboard.mapper.PostMapper;
import com.study.simpleboard.repository.PostReactionRepository;
import com.study.simpleboard.service.exception.InvalidReactionException;
import com.study.simpleboard.service.exception.PostNotFoundException;
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
    private final static Long POST_ID = 1L;
    private final static CustomUserDetails USER_DETAILS = createUserDetails();

    @Mock
    private PostReactionRepository postReactionRepository;

    @Mock
    private PostMapper postMapper;

    @InjectMocks
    private PostReactionService postReactionService;

    private static CustomUserDetails createUserDetails() {
        User user = User.createLocalUser("hong@naver.com",
                "$2a$10$eXthWEeajRbGgRfvlfVBl.LlD6jDWoyAgyRSDa.FdRUTM4vfnYh86",
                "홍길동", "01012345678");
        ReflectionTestUtils.setField(user, "userId", USER_ID);
        return new CustomUserDetails(user);
    }

    @DisplayName("like, dislike 활성화 상태 조회 - like와 dislike 둘 다 존재할 경우")
    @Test
    void getReactionResponse_withLikeAndDislike_returnsList() {
        // 조건: "like, dislike 상태가 둘 다 존재할 경우"
        // 기대 결과: "like=조회 데이터, dislike=조회 데이터 반환"

        // Given: Mock 데이터 정의
        boolean like = true;
        boolean dislike = true;
        List<Reaction> mockReactions = List.of(
                getReaction(ReactionType.LIKE, like), getReaction(ReactionType.DISLIKE, dislike));
        when(postMapper.existsById(POST_ID)).thenReturn(true);
        when(postReactionRepository.findAllReactions(POST_ID, USER_ID)).thenReturn(mockReactions);

        // When: Service 메서드 호출
        PostReactionResponse reactionResponse = postReactionService.getReactionResponse(POST_ID, USER_DETAILS);

        // Then: 결과 검증
        PostReactionResponse response = PostReactionResponse.createDefault()
                .changeLike(like)
                .changeDislike(dislike);
        assertThat(reactionResponse).isEqualTo(response);

        // Then: Mapper 호출 검증
        verify(postReactionRepository).findAllReactions(POST_ID, USER_ID);
    }

    @DisplayName("like, dislike 활성화 상태 조회 - like 상태만 존재할 경우")
    @Test
    void getReactionResponse_withLike_returnsList() {
        // 조건: "like 상태만 존재할 경우"
        // 기대 결과: "like=조회 데이터, dislike=false 반환"

        // Given: Mock 데이터 정의
        boolean like = true;
        List<Reaction> mockReactions = List.of(getReaction(ReactionType.LIKE, like));
        when(postMapper.existsById(POST_ID)).thenReturn(true);
        when(postReactionRepository.findAllReactions(POST_ID, USER_ID)).thenReturn(mockReactions);

        // When: Service 메서드 호출
        PostReactionResponse reactionResponse = postReactionService.getReactionResponse(POST_ID, USER_DETAILS);

        // Then: 결과 검증
        PostReactionResponse response = PostReactionResponse.createDefault().changeLike(like);
        assertThat(reactionResponse).isEqualTo(response);

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
        when(postMapper.existsById(POST_ID)).thenReturn(true);
        when(postReactionRepository.findAllReactions(POST_ID, USER_ID)).thenReturn(mockReactions);

        // When: Service 메서드 호출
        PostReactionResponse reactionResponse = postReactionService.getReactionResponse(POST_ID, USER_DETAILS);

        // Then: 결과 검증
        PostReactionResponse response = PostReactionResponse.createDefault();
        assertThat(reactionResponse).isEqualTo(response);

        // Then: Mapper 호출 검증
        verify(postReactionRepository).findAllReactions(POST_ID, USER_ID);
    }

    @DisplayName("like, dislike 활성화 상태 조회 - 존재하지 않는 postId일 경우")
    @Test
    void getReactionResponse_whenNotFoundPostId_shouldThrowException() {
        // 조건: "존재하지 않는 postId"
        // 기대 결과: "throw exception"

        // Given: Mock 데이터 정의
        when(postMapper.existsById(POST_ID)).thenReturn(false);

        // When: Service 메서드 호출
        // Then: 결과 검증
        assertThatThrownBy(() -> postReactionService.getReactionResponse(POST_ID, USER_DETAILS))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessage(ErrorCode.POST_NOT_FOUND.getMessage());

        // Then: Mapper 호출 검증
        verify(postMapper).existsById(POST_ID);
    }

    @DisplayName("like 또는 dislike 활성화 상태 갱신 - 데이터가 존재할 경우 updateActive 호출")
    @Test
    void saveReactionRequest_whenDataExists_shouldCallUpdateActive() {
        // 조건: "like 데이터가 존재하는 경우"
        // 기대 결과: "데이터 갱신"

        // Given: Mock 데이터 정의
        boolean like = true;
        Boolean dislike = null;
        PostReactionRequest mockRequest = new PostReactionRequest(like, dislike);
        Optional<Reaction> mockReaction = Optional.of(getReaction(ReactionType.LIKE, like));
        when(postMapper.existsById(POST_ID)).thenReturn(true);
        when(postReactionRepository.findReaction(POST_ID, USER_ID, ReactionType.LIKE)).thenReturn(mockReaction);

        // When: Service 메서드 호출
        postReactionService.saveReactionRequest(POST_ID, USER_DETAILS, mockRequest);

        // Then: Mapper 호출 검증
        verify(postReactionRepository).findReaction(POST_ID, USER_ID, ReactionType.LIKE);
        verify(postReactionRepository).updateActive(any(Reaction.class));
    }

    @DisplayName("like 또는 dislike 활성화 상태 갱신 - 데이터가 존재하지 않을 경우 save 호출")
    @Test
    void saveReactionRequest_whenDataExists_shouldCallSave() {
        // 조건: "데이터가 존재하는 않은 경우"
        // 기대 결과: "데이터 저장"

        // Given: Mock 데이터 정의
        boolean like = true;
        Boolean dislike = null;
        PostReactionRequest mockRequest = new PostReactionRequest(like, dislike);
        Optional<Reaction> mockReaction = Optional.empty();
        when(postMapper.existsById(POST_ID)).thenReturn(true);
        when(postReactionRepository.findReaction(POST_ID, USER_ID, ReactionType.LIKE)).thenReturn(mockReaction);

        // When: Service 메서드 호출
        postReactionService.saveReactionRequest(POST_ID, USER_DETAILS, mockRequest);

        // Then: Mapper 호출 검증
        verify(postReactionRepository).findReaction(POST_ID, USER_ID, ReactionType.LIKE);
        verify(postReactionRepository).save(any(Reaction.class));
    }

    @DisplayName("like 또는 dislike 활성화 상태 갱신 - like와 dislike가 둘 다 null일 경우")
    @Test
    void saveReactionRequest_whenLikeAndDislikeIsNull_shouldThrowException() {
        // 조건: "like와 dislike가 둘 다 null일 경우"
        // 기대 결과: "throw InvalidReactionException"

        // Given: Mock 데이터 정의
        Boolean like = null;
        Boolean dislike = null;
        PostReactionRequest invalidRequest = new PostReactionRequest(like, dislike);
        when(postMapper.existsById(POST_ID)).thenReturn(true);

        // When: Service 메서드 호출
        assertThatThrownBy(() -> postReactionService.saveReactionRequest(POST_ID, USER_DETAILS, invalidRequest))
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
        boolean like = true;
        boolean dislike = true;
        PostReactionRequest invalidRequest = new PostReactionRequest(like, dislike);
        when(postMapper.existsById(POST_ID)).thenReturn(true);

        // When: Service 메서드 호출
        assertThatThrownBy(() -> postReactionService.saveReactionRequest(POST_ID, USER_DETAILS, invalidRequest))
                .isInstanceOf(InvalidReactionException.class)
                .hasMessage(ErrorCode.INVALID_REACTION.getMessage())
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_REACTION);

        verify(postReactionRepository,times(0)).findReaction(anyLong(), anyLong(), any(ReactionType.class));
    }

    @DisplayName("like 또는 dislike 활성화 상태 갱신 - 존재하지 않는 postId일 경우")
    @Test
    void saveReactionRequest_whenNotFoundPostId_shouldThrowException() {
        // 조건: "존재하지 않는 postId"
        // 기대 결과: "throw exception"

        // Given: Mock 데이터 정의
        boolean like = true;
        Boolean dislike = null;
        PostReactionRequest mockRequest = new PostReactionRequest(like, dislike);
        when(postMapper.existsById(POST_ID)).thenReturn(false);

        // When: Service 메서드 호출
        // Then: 결과 검증
        assertThatThrownBy(() -> postReactionService.saveReactionRequest(POST_ID, USER_DETAILS, mockRequest))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessage(ErrorCode.POST_NOT_FOUND.getMessage());

        // Then: Mapper 호출 검증
        verify(postMapper).existsById(POST_ID);
    }

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