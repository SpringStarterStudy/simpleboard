package com.study.simpleboard.service;

import com.study.simpleboard.common.exception.CustomException;
import com.study.simpleboard.common.exception.ErrorCode;
import com.study.simpleboard.dto.UserDTO;
import com.study.simpleboard.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("회원가입을 한다.")
    void signUp() {
        // given
        UserDTO userDTO = UserDTO.builder()
                .email("test@test.com")
                .password("password123")
                .name("홍길동")
                .cellPhone("010-1234-5678")
                .build();

        when(userMapper.existsByEmail(userDTO.getEmail())).thenReturn(false);
        when(userMapper.existsByName(userDTO.getName())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userMapper.findByEmail(userDTO.getEmail())).thenReturn(userDTO);

        // when
        UserDTO result = userService.signUp(userDTO);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(userDTO.getEmail());
        assertThat(result.getName()).isEqualTo(userDTO.getName());
    }

    @Test
    @DisplayName("중복된 이메일은 회원가입을 할 수 없다.")
    void existedEmail() {
        // given
        UserDTO userDTO = UserDTO.builder()
                .email("existing@test.com")
                .password("password123")
                .name("홍길동")
                .build();

        when(userMapper.existsByEmail(userDTO.getEmail())).thenReturn(true);

        // when & then
        CustomException exception = assertThrows(CustomException.class,
                () -> userService.signUp(userDTO));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_EMAIL);
    }

}