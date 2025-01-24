package com.study.simpleboard.controller;

import com.google.gson.Gson;
import com.study.simpleboard.dto.PostCreateReq;
import com.study.simpleboard.service.PostService;
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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {
    private final static Long USER_ID = 1L;
    private final static String TITLE = "title";
    private final static String CONTENT = "content";
    private final static Gson gson = new Gson();

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
    }

    @DisplayName("게시글 저장")
    @Test
    void savePost() throws Exception {
        // Given: Mock 데이터 정의
        PostCreateReq mockRequest = getRequest(USER_ID, TITLE, CONTENT);

        // When
        ResultActions resultActions = performPostRequest(mockRequest);

        // Then
        resultActions.andExpect(status().isOk());
        verify(postService).savePost(mockRequest);
    }

    @DisplayName("게시글 저장 - 제목이 null일 경우")
    @Test
    void savePost_titleIsNull_throwException() throws Exception {
        PostCreateReq mockRequest = getRequest(USER_ID, null, CONTENT);

        // When
        ResultActions resultActions = performPostRequest(mockRequest);

        // Then
        resultActions.andExpect(status().isBadRequest());
        verify(postService, times(0)).savePost(mockRequest);
    }

    @DisplayName("게시글 저장 - 제목이 Blank일 경우")
    @Test
    void savePost_titleIsBlank_throwException() throws Exception {
        PostCreateReq mockRequest = getRequest(USER_ID, "  ", CONTENT);

        // When
        ResultActions resultActions = performPostRequest(mockRequest);

        // Then
        resultActions.andExpect(status().isBadRequest());
        verify(postService, times(0)).savePost(mockRequest);
    }

    @DisplayName("게시글 저장 - 내용이 null일 경우")
    @Test
    void savePost_contentIsNull_throwException() throws Exception {
        PostCreateReq mockRequest = getRequest(USER_ID, TITLE, null);

        // When
        ResultActions resultActions = performPostRequest(mockRequest);

        // Then
        resultActions.andExpect(status().isBadRequest());
        verify(postService, times(0)).savePost(mockRequest);
    }

    @DisplayName("게시글 저장 - 내용이 Blank일 경우")
    @Test
    void savePost_contentIsBlank_throwException() throws Exception {
        PostCreateReq mockRequest = getRequest(USER_ID, TITLE, "  ");

        // When
        ResultActions resultActions = performPostRequest(mockRequest);

        // Then
        resultActions.andExpect(status().isBadRequest());
        verify(postService, times(0)).savePost(mockRequest);
    }

    // TODO: userId 검증 추가

    private PostCreateReq getRequest(Long userId, String title, String content) {
        return new PostCreateReq(userId, title, content);
    }

    private ResultActions performPostRequest(PostCreateReq mockRequest) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(mockRequest))
        );
    }
}