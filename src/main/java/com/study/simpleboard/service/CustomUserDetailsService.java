package com.study.simpleboard.service;

import com.study.simpleboard.dto.CustomUserDetails;
import com.study.simpleboard.dto.User;
import com.study.simpleboard.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("이메일로 유저를 찾을 수 없습니다 : " + email);
        }
        return new CustomUserDetails(user);
    }
}
