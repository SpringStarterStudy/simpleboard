package com.study.simpleboard.dto;

// user_social 테이블에 있는 provicer 칼럼에 들어갈 값을 enum으로 관리
public enum SocialType {
    KAKAO("카카오");

    private final String description;

    SocialType (String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}


