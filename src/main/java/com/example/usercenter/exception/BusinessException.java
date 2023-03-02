package com.example.usercenter.exception;

import com.example.usercenter.common.ErrorCode;
import lombok.Data;

/*
 * @author : LYY
 * @date : 2023/2/18 14:07
 * @description
 */
public class BusinessException extends RuntimeException {

    private final int code;

    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode, String description) {
        this(errorCode.getMessage(), errorCode.getCode(), description);
    }

    public BusinessException(ErrorCode errorCode) {
        this(errorCode.getMessage(), errorCode.getCode(), errorCode.getDescription());
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
