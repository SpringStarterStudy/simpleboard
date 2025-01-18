package com.study.simpleboard.common.exception;

import com.study.simpleboard.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // CustomException 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<String>> handleCustomException(CustomException e) {
        log.error("CustomException: {}", e.getErrorCode().getMessage());
        return ResponseEntity
            .status(e.getErrorCode().getStatus())
            .body(ApiResponse.error(e.getErrorCode()));
    }

    // ValidationException 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationException(
        BindingResult bindingResult) {
        log.error("Validation 예외 발생: {}", bindingResult.getAllErrors());
        return ResponseEntity
            .status(ErrorCode.VALIDATION_EXCEPTION.getStatus())
            .body(ApiResponse.error(ErrorCode.VALIDATION_EXCEPTION,
                bindingResult.getAllErrors().get(0).getDefaultMessage()));
    }


}
