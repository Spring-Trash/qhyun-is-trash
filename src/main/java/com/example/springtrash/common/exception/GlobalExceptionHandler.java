package com.example.springtrash.common.exception;


import static com.example.springtrash.common.exception.GlobalErrorCode.INVALID_FIELD_INPUT;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request){
        ErrorCode errorCode = ex.getErrorCode();
//        TODO debug 로그 처리
        return ResponseEntity.status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode, request.getRequestURI()));
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<ErrorResponse> handleServerException(ServerException ex, HttpServletRequest request){
        ErrorCode errorCode = ex.getErrorCode();
        // TODO error 로그 추가
        return ResponseEntity.status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode, request.getRequestURI()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(HttpServletRequest request){
        return ResponseEntity.status(INVALID_FIELD_INPUT.getStatus())
                .body(ErrorResponse.of(INVALID_FIELD_INPUT, request.getRequestURI()));
    }

}
