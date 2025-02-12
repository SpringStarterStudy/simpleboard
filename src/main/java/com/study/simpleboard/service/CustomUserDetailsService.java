package com.study.simpleboard.service;

import com.study.simpleboard.dto.CustomUserDetails;
import com.study.simpleboard.dto.User;
import com.study.simpleboard.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userMapper.findByEmail(email)
                .map(user -> {
                    user.validateEnabled(); // 계정 활성화 여부 검증
                    return new CustomUserDetails(user);
                })
                .orElseThrow(() -> new UsernameNotFoundException("이메일로 유저를 찾을 수 없습니다 : " + email));
    }
}
