package com.study.simpleboard.controller;

import com.study.simpleboard.common.response.ApiResponse;
import com.study.simpleboard.dto.CustomUserDetails;
import com.study.simpleboard.dto.User;
import com.study.simpleboard.dto.request.PostRequestDTO;
import com.study.simpleboard.service.PostService;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {
    private final static long USER_ID = 1L;
    private final static String TITLE = "title";
    private final static String CONTENT = "content";
    private final static CustomUserDetails USER_DETAILS = createUserDetails();

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;
    private Validator validator;

    @BeforeEach
    public void init() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private static CustomUserDetails createUserDetails() {
        User user = User.createLocalUser("hong@naver.com",
                "$2a$10$eXthWEeajRbGgRfvlfVBl.LlD6jDWoyAgyRSDa.FdRUTM4vfnYh86",
                "홍길동", "01012345678");
        ReflectionTestUtils.setField(user, "userId", USER_ID);
        return new CustomUserDetails(user);
    }

    @DisplayName("게시글 저장")
    @Test
    void savePost() {
        // Given: Mock 데이터 정의
        PostRequestDTO.CreateAndUpdate mockRequest = createRequest(TITLE, CONTENT);

        // When
        ApiResponse<Void> response = postController.savePost(mockRequest, USER_DETAILS);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getMessage()).isEqualTo("게시물이 저장되었습니다.");
        assertThat(response.getData()).isNull();
        verify(postService).savePost(mockRequest, USER_ID);
    }

    @DisplayName("게시글 저장 - 제목이 null일 경우")
    @Test
    void savePost_titleIsNull_throwException() throws Exception {
        // given
        String invalidTitle = null;
        PostRequestDTO.CreateAndUpdate mockRequest = createRequest(invalidTitle, CONTENT);

        // When
        Method savePost = PostController.class.getMethod("savePost", PostRequestDTO.CreateAndUpdate.class, CustomUserDetails.class);
        Object[] parameterValues = { mockRequest, USER_DETAILS };
        Set<ConstraintViolation<PostController>> violations = validator.forExecutables()
                .validateParameters(new PostController(postService), savePost, parameterValues);

        // Then
        assertThat(violations).isNotEmpty();    // 예외 발생
        assertThat(violations).anyMatch(violation -> violation.getMessage().contains("제목을 입력해주세요."));
        verify(postService, times(0)).savePost(any(PostRequestDTO.CreateAndUpdate.class), anyLong());
    }

    @DisplayName("게시글 저장 - 제목이 Blank일 경우")
    @Test
    void savePost_titleIsBlank_throwException() throws Exception {
        PostRequestDTO.CreateAndUpdate mockRequest = createRequest("  ", CONTENT);

        // When
        Method savePost = PostController.class.getMethod("savePost", PostRequestDTO.CreateAndUpdate.class, CustomUserDetails.class);
        Object[] parameterValues = { mockRequest, USER_DETAILS };
        Set<ConstraintViolation<PostController>> violations = validator.forExecutables()
                .validateParameters(new PostController(postService), savePost, parameterValues);

        // Then
        assertThat(violations).isNotEmpty();    // 예외 발생
        assertThat(violations).anyMatch(violation -> violation.getMessage().contains("제목을 입력해주세요."));
        verify(postService, times(0)).savePost(any(PostRequestDTO.CreateAndUpdate.class), anyLong());
    }

    @DisplayName("게시글 저장 - 내용이 null일 경우")
    @Test
    void savePost_contentIsNull_throwException() throws Exception {
        PostRequestDTO.CreateAndUpdate mockRequest = createRequest(TITLE, null);

        // When
        Method savePost = PostController.class.getMethod("savePost", PostRequestDTO.CreateAndUpdate.class, CustomUserDetails.class);
        Object[] parameterValues = { mockRequest, USER_DETAILS };
        Set<ConstraintViolation<PostController>> violations = validator.forExecutables()
                .validateParameters(new PostController(postService), savePost, parameterValues);

        // Then
        assertThat(violations).isNotEmpty();    // 예외 발생
        assertThat(violations).anyMatch(violation -> violation.getMessage().contains("내용을 입력해주세요."));
        verify(postService, times(0)).savePost(any(PostRequestDTO.CreateAndUpdate.class), anyLong());
    }

    @DisplayName("게시글 저장 - 내용이 Blank일 경우")
    @Test
    void savePost_contentIsBlank_throwException() throws Exception {
        PostRequestDTO.CreateAndUpdate mockRequest = createRequest(TITLE, "  ");

        // When
        Method savePost = PostController.class.getMethod("savePost", PostRequestDTO.CreateAndUpdate.class, CustomUserDetails.class);
        Object[] parameterValues = { mockRequest, USER_DETAILS };
        Set<ConstraintViolation<PostController>> violations = validator.forExecutables()
                .validateParameters(new PostController(postService), savePost, parameterValues);

        // Then
        assertThat(violations).isNotEmpty();    // 예외 발생
        assertThat(violations).anyMatch(violation -> violation.getMessage().contains("내용을 입력해주세요."));
        verify(postService, times(0)).savePost(any(PostRequestDTO.CreateAndUpdate.class), anyLong());
    }

    private static PostRequestDTO.CreateAndUpdate createRequest(String title, String content) {
        return new PostRequestDTO.CreateAndUpdate(title, content);
    }
}