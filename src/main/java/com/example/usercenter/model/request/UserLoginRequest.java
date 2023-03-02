package com.example.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/*
 * @author : LYY
 * @date : 2023/2/15 17:29
 * @description
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 5404656853735985069L;

    private String userAccout;

    private String userPassword;
}
