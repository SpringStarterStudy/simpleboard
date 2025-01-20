package com.study.simpleboard.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validator;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SignUpRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("유효한 이름이다.")
    void validName() {
        // given
        SignUpRequest request = createValidRequest();

        // when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("이름이 null 값일 때 회원가입에 실패한다.")
    void validName2() {
        // given
        SignUpRequest request = createValidRequest();
        request.setName(null);

        // when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        // then
        assertThat(violations)
                .extracting("message")
                .contains("이름은 필수 입력값입니다.");
    }

    @Test
    @DisplayName("이름이 한 글자일 때 회원가입에 실패한다.")
    void validName3() {
        // given
        SignUpRequest request = createValidRequest();
        request.setName("김");

        // when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        // then
        assertThat(violations)
                .extracting("message")
                .contains("이름은 2자 이상 9자 이하여야 합니다.");
    }

    @Test
    @DisplayName("이름에 허용되지 않은 특수문자가 들어가면 회원가입에 실패한다.")
    void validname4() {
        // given
        SignUpRequest request = createValidRequest();
        request.setName("테스트!");

        // when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        // then
        assertThat(violations)
                .extracting("message")
                .contains("이름은 한글, 영문, 숫자, 언더바(_)만 사용 가능합니다.");
    }

    @Test
    @DisplayName("잘못된 이메일 형식을 입력하면 회원가입 실패를 한다.")
    void validEmail() {
        // given
        SignUpRequest request = createValidRequest();
        request.setEmail("test-email");

        // when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        // then
        assertThat(violations)
                .extracting("message")
                .contains("올바른 이메일 형식이 아닙니다.");
    }

    @Test
    @DisplayName("비밀번호에는 특수문자가 포함되지 않으면 회원가입 실패를 한다.")
    void validPassword2() {
        // given
        SignUpRequest request = createValidRequest();
        request.setPassword("Abcd123456");

        // when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        // then
        assertThat(violations)
                .extracting("message")
                .contains("비밀번호는 특수문자를 포함해야 합니다.");
    }

    @Test
    @DisplayName("올바른 휴대폰 번호 형식이 아니면 회원가입 실패를 한다.")
    void validCellPhone() {
        // given
        SignUpRequest request = createValidRequest();
        request.setCellPhone("010-1234-5678"); // 하이픈이 들어가면 안됨

        // when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        // then
        assertThat(violations)
                .extracting("message")
                .contains("올바른 휴대폰 번호 형식이 아닙니다.");
    }


    private SignUpRequest createValidRequest() {
        return new SignUpRequest(
                "테스트_",
                "test@test.com",
                "Test123!@#",
                "01012345678"
        );
    }

}