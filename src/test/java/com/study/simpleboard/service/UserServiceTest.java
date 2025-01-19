package com.study.simpleboard.service;

import com.study.simpleboard.dto.LoginType;
import com.study.simpleboard.dto.UserDTO;
import com.study.simpleboard.mapper.UserMapper;
import com.study.simpleboard.validator.CellphoneValidator;
import com.study.simpleboard.validator.EmailValidator;
import com.study.simpleboard.validator.NameValidator;
import com.study.simpleboard.validator.PasswordValidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private NameValidator nameValidator;

    @Mock
    private PasswordValidator passwordValidator;

    @Mock
    private EmailValidator emailValidator;

    @Mock
    private CellphoneValidator cellphoneValidator;

    private UserDTO testUser;

    @BeforeEach
    void setUser() {
        testUser = UserDTO.builder()
                .name("test_유저")
                .email("test@test.com")
                .password("Test123!@#")
                .cellPhone("01012345678")
                .isEnabled(true)
                .loginType(LoginType.LOCAL)
                .build();
    }

    @Test
    @DisplayName("회원가입을 성공한다.")
    void signUp() {
        // given
        String password = testUser.getPassword(); // 암호화 하기 전 원본 비밀번호
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        // when
        userService.signUp(testUser);

        // then
        verify(nameValidator).validateName(testUser.getName());
        verify(passwordValidator).validatePassword(password);
        verify(emailValidator).validateEmail(testUser.getEmail());
        verify(cellphoneValidator).validateCellphone(testUser.getCellPhone());
        verify(passwordEncoder).encode(password);
        verify(userMapper).insertUser(testUser);

        assertEquals(encodedPassword, testUser.getPassword());
    }

    @Test
    @DisplayName("잘못된 이름 형식으로 회원가입을 한다.")
    void nameError() {
        // given
        UserDTO invalidName = UserDTO.builder()
                .name("t!!")  // 잘못된 이름
                .email("test@test.com")
                .password("test123!@#")
                .cellPhone("01012345678")
                .isEnabled(true)
                .loginType(LoginType.LOCAL)
                .build();

        // "t!!" 입력을 받으면 예외를 던지도록 설정함
        doThrow(new IllegalArgumentException("잘못된 이름 형식입니다."))
                .when(nameValidator)
                .validateName("t!!");

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            userService.signUp(invalidName);
        });

        // 검증 verify
        verify(nameValidator).validateName("t!!");
        verify(passwordValidator, never()).validatePassword(any());
        verify(emailValidator, never()).validateEmail(any());
        verify(cellphoneValidator, never()).validateCellphone(any());
        verify(passwordEncoder, never()).encode(any());
        verify(userMapper, never()).insertUser(any());
    }

}