package com.study.simpleboard.controller;

import com.google.gson.Gson;
import com.study.simpleboard.common.exception.CustomException;
import com.study.simpleboard.common.exception.ErrorCode;
import com.study.simpleboard.common.exception.GlobalExceptionHandler;
import com.study.simpleboard.domain.enums.ReactionType;
import com.study.simpleboard.dto.CommentReactionRequestDTO;
import com.study.simpleboard.service.CommentReactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CommentReactionControllerTest {

    private final static Long USER_ID = 1L;
    private final static Long COMMENT_ID = 1L;
    private final static Gson gson = new Gson();

    @Mock
    private CommentReactionService commentReactionService;

    @InjectMocks
    private CommentReactionController commentReactionController;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(commentReactionController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @DisplayName("댓글 반응 추가 - 정상 요청")
    @Test
    void putCommentReaction_success() throws Exception {
        CommentReactionRequestDTO mockRequest = getRequest(USER_ID, ReactionType.LIKE);
        ResultActions resultActions = performPostRequest(COMMENT_ID, mockRequest);
        resultActions.andExpect(status().isOk());
        verify(commentReactionService).updateCommentReaction(USER_ID, COMMENT_ID, mockRequest);
    }

    @DisplayName("댓글 반응 추가 - 존재하지 않는 댓글 ID")
    @Test
    void putCommentReaction_nonExistentComment_throwsException() throws Exception {
        CommentReactionRequestDTO mockRequest = getRequest(USER_ID, ReactionType.LIKE);

        doThrow(new CustomException(ErrorCode.COMMENT_NOT_FOUND))
                .when(commentReactionService)
                .updateCommentReaction(USER_ID, COMMENT_ID, mockRequest);

        ResultActions resultActions = performPostRequest(COMMENT_ID, mockRequest);
        resultActions.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("C_001"))
                .andExpect(jsonPath("$.message").value("해당 댓글이 존재하지 않습니다."));
    }

    @DisplayName("댓글 반응 추가 - 반응 유형이 null")
    @Test
    void addCommentReaction_nullReactionType_throwsException() throws Exception {
        CommentReactionRequestDTO mockRequest = getRequest(USER_ID, null);
        ResultActions resultActions = performPostRequest(COMMENT_ID, mockRequest);
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("V_001"))
                .andExpect(jsonPath("$.message").exists());
    }

    private CommentReactionRequestDTO getRequest(Long userId, ReactionType reactionType) {
        return new CommentReactionRequestDTO(userId, COMMENT_ID, reactionType);
    }

    private ResultActions performPostRequest(Long commentId, CommentReactionRequestDTO mockRequest) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.post("/api/comments/{commentId}/reaction", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(mockRequest))
        );
    }
}
