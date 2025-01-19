package com.study.simpleboard.dto;

public enum LoginType {
    LOCAL("일반 회원가입"),
    KAKAO("카카오 로그인");

    private final String description;

    LoginType (String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
