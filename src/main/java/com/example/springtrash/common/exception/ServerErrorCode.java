package com.example.springtrash.common.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ServerErrorCode implements ErrorCode {


    SESSION_INFO_LOST(HttpStatus.INTERNAL_SERVER_ERROR, "S-001", "세션 정보를 DB에서 조회할 수 없습니다..");


    private final HttpStatus status;
    private final String code;
    private final String message;


    @Override
    public String getError() {
        return this.name();
    }
}

