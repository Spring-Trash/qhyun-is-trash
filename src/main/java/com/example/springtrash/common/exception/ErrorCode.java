package com.example.springtrash.common.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    HttpStatus getStatus();
    String getCode();
    String getError();
    String getMessage();
}
