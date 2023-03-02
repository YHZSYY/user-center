package com.example.usercenter.common;

/* 返回结果工具类
 * @author : LYY
 * @date : 2023/2/17 18:35
 * @description
 */
public class ResultUtils {

    /**
     * 成功返回
     * @param data  返回数据
     * @param <T>   数据类型
     * @return  结果封装类
     */
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<T>(0, data, "ok","");
    }

    public static  BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse(errorCode);
    }

    public static BaseResponse error(ErrorCode errorCode, String description) {
        return new BaseResponse(errorCode);
    }

    public static BaseResponse error(int code, String message, String description) {
        return new BaseResponse(code, message, description);
    }

}
