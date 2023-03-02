package com.example.usercenter.service;

import com.example.usercenter.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

/**
* @author Lyy
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2023-02-14 22:19:48
*/
@Service
public interface UserService extends IService<User> {

    /** 用户注册
     *
     * @param userAccount   账户
     * @param userPassword  密码
     * @param checkPassword 校验码
     * @return  用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode);

    /** 用户登录
     *
     * @param userAccout    用户账号
     * @param userPassword  用户密码
     * @return  用户脱敏信息
     */
    User userLogin(String userAccout, String userPassword, HttpServletRequest request);

    /**
     * 用户注销
     * @param request
     * @return
     */
    Integer userLogout(HttpServletRequest request);

    /**
     *  用户信息脱敏
     * @param user
     */
    User getSafetyUser(User user);
}
