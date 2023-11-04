package com.example.springtrash.member.exception;

import com.example.springtrash.common.exception.BusinessException;
import com.example.springtrash.common.exception.ErrorCode;

public class MemberException extends BusinessException {

    public static final MemberException LOGIN_ID_DUPLICATE_EXCEPTION = new MemberException(MemberErrorCode.LOGIN_ID_DUPLICATED);

    private MemberException(ErrorCode errorCode) {
        super(errorCode);
    }


}
