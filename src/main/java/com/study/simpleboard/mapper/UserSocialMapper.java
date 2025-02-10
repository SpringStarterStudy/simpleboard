package com.study.simpleboard.mapper;

import com.study.simpleboard.dto.UserSocial;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserSocialMapper {
    // 소셜 회원가입
    void insertUserSocial(UserSocial userSocial);

    // 소셜 조회
    Optional<UserSocial> findByProviderAndProviderId(String provider, String providerId);

    // 토큰 업데이트
    void updateToken(UserSocial userSocial);
}
