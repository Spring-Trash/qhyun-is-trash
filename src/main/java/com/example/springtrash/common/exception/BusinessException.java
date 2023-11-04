package com.example.springtrash.common.exception;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException{

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
