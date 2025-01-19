package com.study.simpleboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.simpleboard.dto.UserDTO;
import com.study.simpleboard.dto.request.SignUpRequest;
import com.study.simpleboard.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .build();
    }

    @Test
    @DisplayName("회원 가입을 성공한다.")
    void signUp() throws Exception {
        // given
        SignUpRequest request = new SignUpRequest();
        request.setName("test_user");
        request.setEmail("test@test.com");
        request.setPassword("test123!@#");
        request.setCellPhone("01012345678");

        String requestBody = new ObjectMapper().writeValueAsString(request);

        // when & then
        mockMvc.perform(post("/api/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());

        verify(userService, times(1)).signUp(any(UserDTO.class));
    }

}