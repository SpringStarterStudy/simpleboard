package com.study.simpleboard.controller;

import com.study.simpleboard.common.response.ApiResponse;
import com.study.simpleboard.dto.LoginType;
import com.study.simpleboard.dto.User;
import com.study.simpleboard.dto.request.SignUpRequest;
import com.study.simpleboard.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private LocalValidatorFactoryBean validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
    }

    @Test
    @DisplayName("회원 가입을 성공한다.")
    void signUp() {
        // given
        SignUpRequest request = new SignUpRequest();
        request.setEmail("test@test.com");
        request.setPassword("Password123");
        request.setName("테스트");
        request.setCellPhone("01012345678");

        User expectedUser = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .cellPhone(request.getCellPhone())
                .isEnabled(true)
                .loginType(LoginType.LOCAL)
                .build();

        when(userService.signUp(any(User.class))).thenReturn(expectedUser);

        // when
        ApiResponse<User> response = userController.signUp(request);

        // then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("success");
        assertThat(response.getData()).isNotNull();
        assertThat(response.getData().getEmail()).isEqualTo(request.getEmail());
        assertThat(response.getData().getName()).isEqualTo(request.getName());
    }

    @Test
    @DisplayName("이메일 형식이 잘못된 경우 실패한다")
    void signUp_InvalidEmail() {
        // given
        SignUpRequest request = SignUpRequest.builder()
                .email("test-test")
                .password("Test123!@#")
                .name("홍길동")
                .cellPhone("01012345678")
                .build();

        // when
        Errors errors = new BeanPropertyBindingResult(request, "signUpRequest");
        validator.validate(request, errors);

        // then
        assertThat(errors.hasFieldErrors("email")).isTrue();
        assertThat(errors.getFieldError("email").getDefaultMessage())
                .isEqualTo("올바른 이메일 형식이 아닙니다.");
    }

}