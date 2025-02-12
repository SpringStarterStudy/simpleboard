package com.study.simpleboard.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 회원
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "U_001", "이미 가입된 이메일입니다."), // 409
    DUPLICATE_NAME(HttpStatus.CONFLICT, "U_002", "이미 존재하는 이름입니다."), // 409
    SIGNUP_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "U_003", "회원 가입 처리 중 오류가 발생했습니다."), // 505
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U_004", "사용자를 찾을 수 없습니다."), // 404
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "U_005", "비밀번호가 일치하지 않습니다."),
    LOGOUT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "U_006", "로그아웃에 실패했습니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "U_007", "새 비밀번호와 확인 비밀번호가 일치하지 않습니다."),
    SAME_AS_OLD_PASSWORD(HttpStatus.BAD_REQUEST, "U_008", "새 비밀번호가 현재 비밀번호와 동일합니다."),
    DELETE_USER_FAILED(HttpStatus.BAD_REQUEST, "U_009", "회원 탈퇴에 실패했습니다."),
    ACCOUNT_DISABLED(HttpStatus.UNAUTHORIZED, "U_010", "비활성화된 계정입니다."),  // 401

    // 서버
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S_001", "서버 오류가 발생했습니다."),

    // 댓글
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "C_001", "해당 댓글이 존재하지 않습니다."),
    NO_COMMENT_AUTHORITY(HttpStatus.FORBIDDEN, "C_002", "해당 댓글의 작성자가 아닙니다."),

    // 게시물
    PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "P_001", "해당 페이지는 존재하지 않습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "P_002", "해당되는 id의 게시물을 찾을 수 없습니다."),
    NO_POST_AUTHORITY(HttpStatus.FORBIDDEN, "P_003", "작성자가 아닙니다."),

    // ValidationException
    VALIDATION_EXCEPTION(HttpStatus.BAD_REQUEST, "V_001", "잘못된 입력 형식입니다."),

    // 좋아요/싫어요
    INVALID_REACTION(HttpStatus.BAD_REQUEST, "R_001", "잘못된 좋아요/싫어요 반응 요청입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}

