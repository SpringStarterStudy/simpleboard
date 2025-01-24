package com.study.simpleboard.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 서버
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),

    // 댓글
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글이 존재하지 않습니다."),
    NO_COMMENT_AUTHORITY(HttpStatus.FORBIDDEN, "해당 댓글의 작성자가 아닙니다."),

    // 좋아요/싫어요
    INVALID_REACTION(HttpStatus.BAD_REQUEST, "잘못된 좋아요/싫어요 반응 요청입니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
