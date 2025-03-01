package com.study.simpleboard.service.exception;

import com.study.simpleboard.common.exception.CustomException;
import com.study.simpleboard.common.exception.ErrorCode;

public class PostNotFoundException extends CustomException {
    public PostNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
