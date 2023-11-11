package com.example.springtrash.member.exception;

import com.example.springtrash.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@RequiredArgsConstructor
@Getter
public enum MemberErrorCode implements ErrorCode {


    LOGIN_ID_DUPLICATED(HttpStatus.CONFLICT, "M-001", "로그인 아이디가 중복됩니다."),
    WRONG_ID_OR_PASSWORD(HttpStatus.CONFLICT, "M-002", "ID 혹은 비밀번호가 틀렸습니다.");



    private final HttpStatus status;
    private final String code;
    private final String message;


    @Override
    public String getError() {
        return this.name();
    }
}
