package com.study.simpleboard.common.exception;

import jakarta.validation.ValidationException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 서버
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S_001", "서버 오류가 발생했습니다."),

    // 게시물
    PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "P_001", "해당 페이지는 존재하지 않습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "P_002", "해당되는 id의 게시물을 찾을 수 없습니다."),

    // ValidationException
    VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "V_001", "잘못된 입력 형식입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}