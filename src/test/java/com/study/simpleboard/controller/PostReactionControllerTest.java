package com.study.simpleboard.controller;

import com.google.gson.Gson;
import com.study.simpleboard.dto.PostReactionReq;
import com.study.simpleboard.dto.PostReactionResp;
import com.study.simpleboard.service.PostReactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PostReactionControllerTest {
    private final static Long USER_ID = 1L;
    private final static Long POST_ID = 15L;

    @Mock
    private PostReactionService postReactionService;

    @InjectMocks
    private PostReactionController postReactionController;
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(postReactionController).build();
    }

    @DisplayName("like, dislike 활성화 여부 조회")
    @Test
    void getReaction() throws Exception {
        // Given: Mock 데이터 정의
        PostReactionResp mockResponse = getResponse();
        when(postReactionService.getReactionResponse(POST_ID, USER_ID)).thenReturn(mockResponse);

        // When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/posts/{postId}/reaction", POST_ID)
                        .param("userId", String.valueOf(USER_ID))
        );

        // Then
        MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.like").exists()) // "like" 필드 존재 확인
                .andExpect(jsonPath("$.like.active").value(true)) // "like.active" 값 확인
                .andExpect(jsonPath("$.dislike").exists()) // "dislike" 필드 존재 확인
                .andExpect(jsonPath("$.dislike.active").value(false)) // "dislike.active" 값 확인
                .andReturn();
        PostReactionResp response = gson().fromJson(mvcResult.getResponse().getContentAsString(), PostReactionResp.class);
        assertThat(response).isEqualTo(mockResponse);
        verify(postReactionService).getReactionResponse(POST_ID, USER_ID);
    }

    @DisplayName("like 또는 dislike 활성화 상태에 대한 요청을 받아서 저장")
    @Test
    void saveReaction() throws Exception {
        // Given: Mock 데이터 정의
        PostReactionReq mockRequest = getRequest();

        // When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/posts/{postId}/reaction", POST_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson().toJson(mockRequest))
        );

        // Then
        resultActions.andExpect(status().isNoContent());
        verify(postReactionService).saveReactionRequest(POST_ID, mockRequest);
    }

    private Gson gson() {
        return new Gson();
    }

    private PostReactionReq getRequest() {
        return new PostReactionReq(USER_ID, null, true);
    }

    private PostReactionResp getResponse() {
        return PostReactionResp.createDefault()
                .changeLike(true);
    }
}