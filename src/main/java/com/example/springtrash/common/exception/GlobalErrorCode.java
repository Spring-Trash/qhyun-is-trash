package com.example.springtrash.common.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum GlobalErrorCode implements ErrorCode {


    INVALID_FIELD_INPUT(HttpStatus.BAD_REQUEST, "G-001", "입력하신 내용을 다시 확인해주세요."),

    UN_AUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "G-002", "인증되지 않은 접근입니다."),



    ;



    private final HttpStatus status;
    private final String code;
    private final String message;


    @Override
    public String getError() {
        return this.name();
    }
}
