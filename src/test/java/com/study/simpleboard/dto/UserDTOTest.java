package com.study.simpleboard.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    @Test
    @DisplayName("유저 객체를 생성한다.")
    void UserDto() {
        // given
        LocalDateTime now = LocalDateTime.now();

        // when
        User userDTO = User.builder()
                .userId(1L)
                .name("test_유저")
                .email("test@test.com")
                .password("test123!@#")
                .cellPhone("01012345678")
                .isEnabled(true)
                .loginType(LoginType.LOCAL)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // then
        assertAll(
                () -> assertEquals(1L, userDTO.getUserId()),
                () -> assertEquals("test_유저", userDTO.getName()),
                () -> assertEquals("test@test.com", userDTO.getEmail()),
                () -> assertEquals("test123!@#", userDTO.getPassword()),
                () -> assertEquals("01012345678", userDTO.getCellPhone()),
                () -> assertTrue(userDTO.isEnabled()),
                () -> assertEquals(LoginType.LOCAL, userDTO.getLoginType()),
                () -> assertEquals(now, userDTO.getCreatedAt()),
                () -> assertEquals(now, userDTO.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("LoginType ENUM에는 'LOCAL, KAKAO' 두 가지 타입이 존재한다.")
    void LoginTypeEnum() {
        // then
        assertAll(
                () -> assertEquals(2, LoginType.values().length)
        );
    }

}