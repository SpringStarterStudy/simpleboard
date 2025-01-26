package com.study.simpleboard.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.study.simpleboard.common.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private final int status;
    private final String message;
    private final T data;

    private ApiResponse(HttpStatus status, String message, T data) {
        this.status = status.value();
        this.message = message;
        this.data = data;
    }

    private ApiResponse(HttpStatus status, String message) {
        this(status, message, null);
    }

    // 아래 메서드 수정
    public static ApiResponse<Void> success() {
        return new ApiResponse<>(HttpStatus.OK, "success");
    }

    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(HttpStatus.OK, message);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(HttpStatus.OK, "success", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(HttpStatus.OK, message, data);
    }

    public static ApiResponse<String> error(ErrorCode errorCode) {
        return new ApiResponse<>(errorCode.getStatus(), errorCode.getCode(),
                errorCode.getMessage());
    }

    // MethodArgumentNotValidException 발생 시 사용됨
    public static ApiResponse<String> error(ErrorCode errorCode, String customMessage) {
        return new ApiResponse<>(errorCode.getStatus(), errorCode.getCode(), customMessage);
    }
}