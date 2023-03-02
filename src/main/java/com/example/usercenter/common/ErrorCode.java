package com.example.usercenter.common;

/**
 * 错误码
 */
public enum ErrorCode {
    // 成功
    SUCCESS(0, "ok", "成功"),
    // 请求参数错误
    PARAMS_ERROR(40000, "请求参数错误", "无"),
    NULL_ERROR(40001, "请求数据为空", "无"),
    NOT_LOGIN(40100, "未登录", "无"),
    NO_AUTH(40101, "无权限", "无"),
    SYSTEM_ERROR(50000, "系统内部异常", "无"),
    DATA_OPERATION_ERROR(60000, "数据库操作失败", "无");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 状态码信息
     */
    private final String message;

    /**
     * 状态码描述
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
