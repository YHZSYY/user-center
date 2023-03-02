package com.example.usercenter.common;

import lombok.Data;
import org.apache.logging.log4j.message.Message;

import java.io.Serializable;

/*  封装返回结果类
 * @author : LYY
 * @date : 2023/2/17 18:28
 * @description
 */
@Data
public class BaseResponse <T> implements Serializable {

    private static final long serialVersionUID = 1546777672313499959L;

    private int code;

    private T data;

    private String message;

    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "","");

    }
    public BaseResponse(int code, String message) {
        this(code, null, message,"description");

    }

    public BaseResponse(int code, String message, String description) {
        this(code, null, message, description);

    }

    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }

    public BaseResponse(ErrorCode errorCode, String description){
        this(errorCode.getCode(), null, errorCode.getMessage(), description);
    }
}
