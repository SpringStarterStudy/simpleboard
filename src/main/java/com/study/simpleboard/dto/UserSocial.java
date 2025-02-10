package com.study.simpleboard.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSocial {
    private Long userSocialId;
    private Long userId;
    private String provider;                // 소셜 서비스 제공자 ex) "KAKAO"
    private String providerId;              // 소셜 서비스의 사용자 ID ex) 카카오 회원번호
    private String accessToken;             // 액세스 토큰
    private String refreshToken;            // 리프레시 토큰
    private LocalDateTime tokenExpiryDate;  // 토큰 만료일

    // 카카오 소셜 정보 생성
    public static UserSocial createKakaoUserSocial(Long userId, String providerId, KakaoToken kakaoToken) {
        return UserSocial.builder()
                .userId(userId)
                .provider(String.valueOf(SocialType.KAKAO))
                .providerId(providerId)
                .accessToken(kakaoToken.getAccessToken())
                .refreshToken(kakaoToken.getRefreshToken())
                .tokenExpiryDate(LocalDateTime.now().plusSeconds(kakaoToken.getExpiresIn()))
                .build();
    }

    // 토큰 업데이트
    public void updateToken(KakaoToken kakaoToken) {
        this.accessToken = kakaoToken.getAccessToken();
        this.refreshToken = kakaoToken.getRefreshToken();
        this.tokenExpiryDate = LocalDateTime.now().plusSeconds(kakaoToken.getExpiresIn());
    }
}
