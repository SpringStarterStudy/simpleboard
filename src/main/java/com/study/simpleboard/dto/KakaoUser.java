package com.study.simpleboard.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 카카오에 등록되어 있는 계정 정보
@Getter
@NoArgsConstructor
public class KakaoUser {
    private Long id;
    private KakaoAccount kakaoAccount;

    @Getter
    @NoArgsConstructor
    public static class KakaoAccount {
        private String email;
        private Profile profile;

        @Getter
        @NoArgsConstructor
        public static class Profile {
            private String nickname;
        }
    }
}
