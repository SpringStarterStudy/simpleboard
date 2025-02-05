package com.study.simpleboard.service.exception;

import com.study.simpleboard.common.exception.CustomException;
import com.study.simpleboard.common.exception.ErrorCode;

public class InvalidReactionException extends CustomException {
    public InvalidReactionException(ErrorCode errorCode) {
        super(errorCode);
    }
}
