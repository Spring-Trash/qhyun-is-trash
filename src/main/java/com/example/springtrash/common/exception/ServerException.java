package com.example.springtrash.common.exception;

import lombok.Getter;

@Getter
public class ServerException extends RuntimeException{

    private final ErrorCode errorCode;

    public ServerException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}

