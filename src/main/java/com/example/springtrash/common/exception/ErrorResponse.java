package com.example.springtrash.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
public class ErrorResponse {

    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String message;
    private final String path;


    @Builder
    private ErrorResponse(int status, String error, String code, String message, String path) {
        this.status = status;
        this.error = error;
        this.code = code;
        this.message = message;
        this.path = path;
    }

    public static ErrorResponse of(ErrorCode errorCode, String path){
        return ErrorResponse.builder()
                .error(errorCode.getError())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .path(path)
                .status(errorCode.getStatus().value())
                .build();
    }
}
