package com.example.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/*  注册用户请求体
 * @author : LYY
 * @date : 2023/2/15 17:14
 * @description
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -4853927297482331694L;

    private String userAccout;

    private String userPassword;

    private String checkPassword;

    private String planetCode;

}
