package com.study.simpleboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

// Kakao OAuth API에서 토큰을 받아올 때 사용되는 DTO
// 카카오 서버로부터 받은 JSON 응답을 자바 객체로 변환 -> 역직렬화
@Getter
@NoArgsConstructor
public class KakaoToken {
    @JsonProperty("access_token")   // API 호출 시 즉시 사용되고 버려질 토큰
    private String accessToken;

    @JsonProperty("token_type")     // 토큰 타입 정보
    private String tokenType;

    @JsonProperty("refresh_token")  // DB에 저장될 리프레시 토큰
    private String refreshToken;

    @JsonProperty("expires_in")     // 토큰 만료 시간을 계산하는데 사용
    private int expiresIn;
}
