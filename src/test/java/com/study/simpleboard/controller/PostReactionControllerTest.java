package com.study.simpleboard.controller;

import com.study.simpleboard.common.response.ApiResponse;
import com.study.simpleboard.dto.CustomUserDetails;
import com.study.simpleboard.dto.PostReactionReq;
import com.study.simpleboard.dto.PostReactionResp;
import com.study.simpleboard.dto.User;
import com.study.simpleboard.service.PostReactionService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostReactionControllerTest {
    private final static long USER_ID = 1L;
    private final static long POST_ID = 1L;

    @Mock
    private PostReactionService postReactionService;

    @InjectMocks
    private PostReactionController postReactionController;
    private Validator validator;

    @BeforeEach
    public void init() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @DisplayName("like, dislike 활성화 여부 조회")
    @Test
    void getReaction() throws Exception {
        // Given: Mock 데이터 정의
        PostReactionResp mockResponse = getResponse();
        when(postReactionService.getReactionResponse(POST_ID, USER_ID)).thenReturn(mockResponse);

        // When
        ApiResponse<PostReactionResp> response = postReactionController.getReaction(POST_ID, createUserDetails());

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getMessage()).isEqualTo("success");
        assertThat(response.getData()).isNotNull();
        assertThat(response.getData()).isEqualTo(mockResponse);
        verify(postReactionService).getReactionResponse(POST_ID, USER_ID);
    }

    @DisplayName("like 또는 dislike 활성화 상태에 대한 요청을 받아서 저장")
    @Test
    void saveReaction() throws Exception {
        // Given: Mock 데이터 정의
        PostReactionReq mockRequest = getRequest();

        // When
        ResponseEntity<Void> response = postReactionController.saveReaction(POST_ID, mockRequest, createUserDetails());

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(postReactionService).saveReactionRequest(POST_ID, USER_ID, mockRequest);
    }

    @DisplayName("like, dislike 활성화 여부 조회 - postId가 양수가 아닐 경우")
    @Test
    void getReaction_whenPostIdIsNotPositive_shouldThrowException() throws Exception {
        // given
        long invalidPostId = 0L;

        // when
        Method getReaction = PostReactionController.class.getMethod("getReaction", Long.class, CustomUserDetails.class);
        Object[] parameterValues = { invalidPostId, createUserDetails() };
        Set<ConstraintViolation<PostReactionController>> violations = validator.forExecutables()
                .validateParameters(
                        new PostReactionController(postReactionService), getReaction, parameterValues);

        // then
        assertThat(violations).isNotEmpty();    // 예외 발생
        assertThat(violations).anyMatch(violation -> violation.getMessage().contains("0보다 커야 합니다"));
    }

    private static CustomUserDetails createUserDetails() {
        User user = User.createLocalUser("hong@naver.com",
                "$2a$10$eXthWEeajRbGgRfvlfVBl.LlD6jDWoyAgyRSDa.FdRUTM4vfnYh86",
                "홍길동", "01012345678");
        ReflectionTestUtils.setField(user, "userId", USER_ID);
        return new CustomUserDetails(user);
    }

    private PostReactionReq getRequest() {
        return new PostReactionReq(null, true);
    }

    private PostReactionResp getResponse() {
        return PostReactionResp.createDefault()
                .changeLike(true);
    }
}