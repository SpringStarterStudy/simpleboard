package com.study.simpleboard;

import com.google.gson.Gson;
import com.study.simpleboard.common.exception.ErrorCode;
import com.study.simpleboard.dto.PostCreateReq;
import com.study.simpleboard.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Sql(scripts = "/post_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class PostIntegrationTest {
    private final static Long USER_ID = 1L;
    private final static Gson gson = new Gson();

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

//    @Autowired
//    private PostRepository postRepository;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("게시글 작성")
    void savePost() throws Exception {
        // given
        String title = "제목 테스트";
        String content = "내용 테스트";
        PostCreateReq mockRequest = createRequest(title, content);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(mockRequest))
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("게시글이 저장되었습니다."));

        // TODO: 저장된 게시글 조회 후 db에 저장되었는지 검증 필요
    }

    @Test
    @DisplayName("게시글 작성 - 제목이 null일 경우")
    void savePost_whenTitleIsNull_returnError() throws Exception {
        // given
        String title = null;
        String content = "내용 테스트";
        PostCreateReq mockRequest = createRequest(title, content);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(mockRequest))
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ErrorCode.VALIDATION_EXCEPTION.getStatus().value()))
//                .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_EXCEPTION.getCode()))
                .andExpect(jsonPath("$.message").value("제목을 입력해주세요."));
    }

    @Test
    @DisplayName("게시글 작성 - 제목이 공백일 경우")
    void savePost_whenTitleIsBlank_returnError() throws Exception {
        // given
        String title = "    ";
        String content = "내용 테스트";
        PostCreateReq mockRequest = createRequest(title, content);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(mockRequest))
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ErrorCode.VALIDATION_EXCEPTION.getStatus().value()))
//                .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_EXCEPTION.getCode()))
                .andExpect(jsonPath("$.message").value("제목을 입력해주세요."));
    }

    @Test
    @DisplayName("게시글 작성 - 내용이 null일 경우")
    void savePost_whenContentIsNull_returnError() throws Exception {
        // given
        String title = "제목 테스트";
        String content = null;
        PostCreateReq mockRequest = createRequest(title, content);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(mockRequest))
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ErrorCode.VALIDATION_EXCEPTION.getStatus().value()))
//                .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_EXCEPTION.getCode()))
                .andExpect(jsonPath("$.message").value("내용을 입력해주세요."));
    }

    @Test
    @DisplayName("게시글 작성 - 내용이 공백일 경우")
    void savePost_whenContextIsBlank_returnError() throws Exception {
        // given
        String title = "내용 테스트";
        String content = "    ";
        PostCreateReq mockRequest = createRequest(title, content);

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(mockRequest))
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ErrorCode.VALIDATION_EXCEPTION.getStatus().value()))
//                .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_EXCEPTION.getCode()))
                .andExpect(jsonPath("$.message").value("내용을 입력해주세요."));
    }

    // TODO: userId 검증 추가

    private static PostCreateReq createRequest(String title, String content) {
        return new PostCreateReq(USER_ID, title, content);
    }
}
