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
    @DisplayName("유효한 이름입니다.")
    void validName() {
        // given
        SignUpRequest request = createValidRequest();

        // when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();

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