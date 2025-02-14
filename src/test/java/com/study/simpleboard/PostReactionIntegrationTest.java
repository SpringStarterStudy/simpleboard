package com.study.simpleboard;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.study.simpleboard.common.exception.ErrorCode;
import com.study.simpleboard.common.response.ApiResponse;
import com.study.simpleboard.domain.Reaction;
import com.study.simpleboard.domain.enums.ReactionType;
import com.study.simpleboard.domain.enums.TargetType;
import com.study.simpleboard.dto.CustomUserDetails;
import com.study.simpleboard.dto.PostReactionReq;
import com.study.simpleboard.dto.PostReactionResp;
import com.study.simpleboard.mapper.UserMapper;
import com.study.simpleboard.repository.PostReactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Sql(scripts = "/post_reaction_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class PostReactionIntegrationTest {
    private final static Long POST_ID = 1L;
    private final static Long USER_ID = 1L;
    private final static Gson gson = new Gson();

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Autowired
    private PostReactionRepository postReactionRepository;

    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        CustomUserDetails userDetails = createUserDetails();
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
    }

    @Test
    @DisplayName("like, dislike 활성화 여부 조회 - reaction 정보가 db에 없을 경우")
    void getReaction_whenNotExistReaction_returnDefaultReaction() throws Exception {
        // given
        boolean like = false;
        boolean dislike = false;
        PostReactionResp mockResponse = PostReactionResp.createDefault();

        // When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/posts/{postId}/reaction", POST_ID)
        );

        // Then
        MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.like").exists()) // "like" 필드 존재 확인
                .andExpect(jsonPath("$.data.like.active").value(like)) // "like.active" 값 확인
                .andExpect(jsonPath("$.data.dislike").exists()) // "dislike" 필드 존재 확인
                .andExpect(jsonPath("$.data.dislike.active").value(dislike)) // "dislike.active" 값 확인
                .andReturn();
        ApiResponse<PostReactionResp> response = gson.fromJson(
                mvcResult.getResponse().getContentAsString(),
                TypeToken.getParameterized(ApiResponse.class, PostReactionResp.class).getType());
        assertThat(response.getData()).isEqualTo(mockResponse);
    }

    @Test
    @DisplayName("like, dislike 활성화 여부 조회 - reaction 정보가 db에 있을 경우")
    void getReaction_whenExistReaction_returnDbReaction() throws Exception {
        // given
        boolean like = true;
        boolean dislike = false;
        postReactionRepository.save(getReaction(ReactionType.LIKE, like));
        PostReactionResp mockResponse = PostReactionResp.createDefault().changeLike(like);

        // When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/posts/{postId}/reaction", POST_ID)
        );

        // Then
        MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.like").exists()) // "like" 필드 존재 확인
                .andExpect(jsonPath("$.data.like.active").value(like)) // "like.active" 값 확인
                .andExpect(jsonPath("$.data.dislike").exists()) // "dislike" 필드 존재 확인
                .andExpect(jsonPath("$.data.dislike.active").value(dislike)) // "dislike.active" 값 확인
                .andReturn();
        ApiResponse<PostReactionResp> response = gson.fromJson(
                mvcResult.getResponse().getContentAsString(),
                TypeToken.getParameterized(ApiResponse.class, PostReactionResp.class).getType());
        assertThat(response.getData()).isEqualTo(mockResponse);
    }

    @Test
    @DisplayName("like, dislike 활성화 여부 조회 - postId가 양수가 아닐 경우")
    void getReaction_whenPostIdIsNotPositive_returnError() throws Exception {
        // given
        long invalidPostId = 0L;

        // When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/posts/{postId}/reaction", invalidPostId)
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ErrorCode.VALIDATION_EXCEPTION.getStatus().value()))
                .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_EXCEPTION.getCode()))
                .andExpect(jsonPath("$.message").value("postId: must be greater than 0"));
    }

    @Test
    @DisplayName("like 또는 dislike 활성화 상태에 대한 요청을 받아서 저장 - reaction 정보가 db에 없을 경우")
    void saveReactionRequest_whenNotExistReaction() throws Exception {
        // given
        Boolean like = null;
        Boolean dislike = true;
        PostReactionReq mockRequest = new PostReactionReq(like, dislike);

        // When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/posts/{postId}/reaction", POST_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(mockRequest))
        );

        // then
        resultActions.andExpect(status().isNoContent());
        Optional<Reaction> postReaction = postReactionRepository.findReaction(POST_ID, USER_ID, ReactionType.DISLIKE);
        assertThat(postReaction.isPresent()).isTrue();
        Reaction reaction = postReaction.get();
        assertThat(reaction.getUserId()).isEqualTo(USER_ID);
        assertThat(reaction.getTargetId()).isEqualTo(POST_ID);
        assertThat(reaction.getTargetType()).isEqualTo(TargetType.POST);
        assertThat(reaction.getReactionType()).isEqualTo(ReactionType.DISLIKE);
        assertThat(reaction.getActive()).isEqualTo(dislike);
    }

    @Test
    @DisplayName("like 또는 dislike 활성화 상태에 대한 요청을 받아서 저장 - reaction 정보가 db에 있을 경우")
    void saveReactionRequest_whenExistReaction() throws Exception {
        // given
        boolean beforeDislike = true;
        boolean afterDislike = false;
        postReactionRepository.save(getReaction(ReactionType.DISLIKE, beforeDislike));
        PostReactionReq mockRequest = new PostReactionReq(null, afterDislike);

        // When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/posts/{postId}/reaction", POST_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(mockRequest))
        );

        // then
        resultActions.andExpect(status().isNoContent());
        Optional<Reaction> postReaction = postReactionRepository.findReaction(POST_ID, USER_ID, ReactionType.DISLIKE);
        assertThat(postReaction.isPresent()).isTrue();
        Reaction reaction = postReaction.get();
        assertThat(reaction.getUserId()).isEqualTo(USER_ID);
        assertThat(reaction.getTargetId()).isEqualTo(POST_ID);
        assertThat(reaction.getTargetType()).isEqualTo(TargetType.POST);
        assertThat(reaction.getReactionType()).isEqualTo(ReactionType.DISLIKE);
        assertThat(reaction.getActive()).isEqualTo(afterDislike);
    }

    @Test
    @DisplayName("like 또는 dislike 활성화 상태에 대한 요청을 받아서 저장 - like와 dislike가 둘 다 null일 경우")
    void saveReactionRequest_whenLikeAndDislikeIsNull_returnError() throws Exception {
        // given
        Boolean like = null;
        Boolean dislike = null;
        PostReactionReq invalidRequest = new PostReactionReq(like, dislike);

        // When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/posts/{postId}/reaction", POST_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(invalidRequest))
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ErrorCode.INVALID_REACTION.getStatus().value()))
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_REACTION.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_REACTION.getMessage()));
    }

    @Test
    @DisplayName("like 또는 dislike 활성화 상태에 대한 요청을 받아서 저장 - like와 dislike의 데이터가 둘 다 존재할 경우")
    void saveReactionRequest_whenLikeAndDislikeExist_returnError() throws Exception {
        // given
        boolean like = true;
        boolean dislike = false;
        PostReactionReq invalidRequest = new PostReactionReq(like, dislike);

        // When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/posts/{postId}/reaction", POST_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(invalidRequest))
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ErrorCode.INVALID_REACTION.getStatus().value()))
                .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_REACTION.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_REACTION.getMessage()));
    }

    // TODO: userId 검증 테스트 코드 추가
    //  postId 검증 테스트 추가

    private static Reaction getReaction(ReactionType reactionType, boolean active) {
        return reactionType == ReactionType.LIKE
                ? Reaction.of(USER_ID, POST_ID, new PostReactionReq(active, null))
                : Reaction.of(USER_ID, POST_ID, new PostReactionReq(null, active));
    }

    private CustomUserDetails createUserDetails() {
        return new CustomUserDetails(userMapper.findById(1L).get());
    }
}
