package com.study.simpleboard.service;

import com.study.simpleboard.common.exception.CustomException;
import com.study.simpleboard.common.exception.ErrorCode;
import com.study.simpleboard.controller.UserController;
import com.study.simpleboard.dto.CustomUserDetails;
import com.study.simpleboard.dto.User;
import com.study.simpleboard.dto.UserSocial;
import com.study.simpleboard.mapper.UserMapper;
import com.study.simpleboard.mapper.UserSocialMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class KakaoUserServiceTest {
    @Mock
    private UserMapper userMapper;

    @Mock
    private UserSocialMapper userSocialMapper;

    @InjectMocks
    private KakaoUserService kakaoUserService;

    @Test
    public void kakaoLogout() {
        // Given
        Long userId = 123L;
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getUserId()).thenReturn(userId);

        UserSocial userSocial = mock(UserSocial.class);
        when(userSocialMapper.findByUserId(userId)).thenReturn(Optional.of(userSocial));

        // void 메소드는 doNothing으로 모킹
        doNothing().when(userSocialMapper).deleteToken(userId);

        // SecurityContext가 비어있지 않은지 확인하기 위해 설정
        SecurityContextHolder.getContext().setAuthentication(mock(org.springframework.security.core.Authentication.class));

        // When
        kakaoUserService.kakaoLogout(userDetails);

        // Then
        verify(userSocialMapper, times(1)).findByUserId(userId);
        verify(userSocialMapper, times(1)).deleteToken(userId);

        // SecurityContext가 비워졌는지 확인
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void deleteKakaoUser() {
        // Given
        Long userId = 123L;
        User user = mock(User.class); // User 클래스의 mock 객체 생성

        // 사용자가 존재할 때 (첫 번째 호출은 사용자 있음, 두 번째 호출은 null)
        when(userMapper.findById(userId))
                .thenReturn(Optional.of(user))  // 첫 번째 호출
                .thenReturn(Optional.empty());  // 두 번째 호출 (삭제 확인)

        doNothing().when(userSocialMapper).deleteUserSocial(userId);
        doNothing().when(userMapper).deleteUser(userId);

        // When
        kakaoUserService.deleteKakaoUser(userId);

        // Then
        // times(2)로 변경하여 findById가 2번 호출되는 것을 검증
        verify(userMapper, times(2)).findById(userId);
        verify(userSocialMapper, times(1)).deleteUserSocial(userId);
        verify(userMapper, times(1)).deleteUser(userId);
    }

    @Test
    public void deleteKakaoUser2() {
        // Given
        Long userId = 999L;

        // 사용자가 존재하지 않을 때
        when(userMapper.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            kakaoUserService.deleteKakaoUser(userId);
        });

        // 예외 메시지 확인
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());

        // 사용자가 없으므로 다음 메소드들은 호출되지 않아야 함
        verify(userSocialMapper, never()).deleteUserSocial(userId);
        verify(userMapper, never()).deleteUser(userId);
    }
}